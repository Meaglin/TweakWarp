package com.guntherdw.bukkit.TweakWarp.DataSource;

import com.guntherdw.bukkit.TweakWarp.TweakWarp;
import com.guntherdw.bukkit.TweakWarp.Warp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GuntherDW
 */
public class Database {

    private TweakWarp plugin;
    private String db, user, pass, dbhost;

    public static final String SAVE_WARP = "REPLACE INTO `warps` (name,x,y,z,pitch,yaw,world,warpgroup,accessgroup) VALUES (?,?,?,?,?,?,?,?,?)";
    
    //public static final String GET_WARP_BY_ID = "SELECT * FROM `warps` WHERE `name` LIKE ? AND `warpgroup` LIKE ?";
    //public static final String GET_WARP_BY_NAME = "SELECT * FROM `warps` WHERE `id` = ? LIMIT 1";
    public static final String GET_ALL_WARPS = "SELECT * FROM `warps`";
    
    public static final String DELETE_WARP = "DELETE FROM `warps` WHERE `id` = ? LIMIT 1";
    
    
    
    public Database(TweakWarp instance) {
        this.plugin = instance;
        this.loadDriver();
        this.setupConnection();
    }

    public void initConfig()
    {
        try{
            plugin.getConfiguration().setProperty("database", "databasename");
            plugin.getConfiguration().setProperty("username", "database-username");
            plugin.getConfiguration().setProperty("password", "database-password");
        } catch (Throwable e)
        {
            plugin.getLogger().severe("[TweakWarp] There was an exception while we were saving the config, be sure to doublecheck!");
        }
    }

    private void loadDriver() {
        final String driverName = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            //return null;
        }
    }

    private Connection getConnection() throws SQLException
    {
        String url = "jdbc:mysql://"+dbhost+":3306/" + db;
        return DriverManager.getConnection(url + "?autoReconnect=true&user=" + user + "&password=" + pass);
    }

    public void setupConnection() {
        this.dbhost = plugin.getConfiguration().getString("dbhost");
        this.db =  plugin.getConfiguration().getString("database");
        this.user = plugin.getConfiguration().getString("username");
        this.pass = plugin.getConfiguration().getString("password");
    }

    public List<Warp> getAllWarps() {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        List<Warp> warps = new ArrayList<Warp>();
        try {
        	conn = getConnection();
            st = conn.prepareStatement(GET_ALL_WARPS);
            rs = st.executeQuery();
            while(rs.next()) {
                Warp temp = new Warp(
                		rs.getDouble(3),
                        rs.getDouble(4),
                        rs.getDouble(5),
                        rs.getFloat(6),
                        rs.getFloat(7),
                        rs.getString(2),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10)
                );
                temp.setId(rs.getInt(1));
                warps.add(temp);

            }
        } catch (Exception e) {
        	TweakWarp.log.warning("[TweakWarp]Error loading warps:");
            e.printStackTrace();
        } finally {
        	try {
        		if(conn != null) conn.close();
        		if(st != null) st.close();
        		if(rs != null) rs.close();
        	} catch(Exception e) {}
        }
        return warps;
    }

    /* 
    public Warp getWarp(int id) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Warp temp = null;
        try {
        	conn = getConnection();
            st = conn.prepareStatement(GET_WARP_BY_ID);
            st.setInt(1, id);
            rs = st.executeQuery();
            if(rs.next()) {
                temp = new Warp(rs.getDouble("x"),
                        rs.getDouble("y"),
                        rs.getDouble("z"),
                        rs.getFloat("pitch"),
                        rs.getFloat("yaw"),
                        rs.getString("name"),
                        rs.getString("world"),
                        rs.getString("warpgroup"),
                        rs.getString("accessgroup")
                );
                temp.setId(rs.getInt("id"));
            }

        } catch (Exception e) {
        	TweakWarp.log.warning("[TweakWarp]Error getting warp with id " + id + ":");
            e.printStackTrace();
        } finally {
        	try {
        		if(conn != null) conn.close();
        		if(st != null) st.close();
        		if(rs != null) rs.close();
        	} catch(Exception e) {}
        }
        return temp;
    }
     
    public Warp getWarp(String name, String warpgroup) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Warp temp = null;
        try {
            conn = getConnection();
            st = conn.prepareStatement(GET_WARP_BY_NAME);
            st.setString(1, name);
            st.setString(2, warpgroup);
            rs = st.executeQuery();
            if(rs.next()) {
                temp = new Warp(rs.getDouble("x"),
                        rs.getDouble("y"),
                        rs.getDouble("z"),
                        rs.getFloat("pitch"),
                        rs.getFloat("yaw"),
                        rs.getString("name"),
                        rs.getString("world"),
                        rs.getString("warpgroup"),
                        rs.getString("accessgroup")
                );
                temp.setId(rs.getInt("id"));
            }

        } catch (Exception e) {
        	TweakWarp.log.warning("[TweakWarp]Error getting warp " + name + "[" + warpgroup + "]:");
            e.printStackTrace();
        } finally {
        	try {
        		if(conn != null) conn.close();
        		if(st != null) st.close();
        		if(rs != null) rs.close();
        	} catch(Exception e) {}
        }
        return temp;
    }
    */
    public boolean saveWarp(Warp warp) { 
        if(warp==null) return false;

        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            st = conn.prepareStatement(SAVE_WARP, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, warp.getName());
            st.setDouble(2, warp.getX());
            st.setDouble(3, warp.getY());
            st.setDouble(4, warp.getZ());
            st.setFloat (5, warp.getPitch());
            st.setFloat (6, warp.getYaw());
            st.setString(7, warp.getWorld());
            st.setString(8, warp.getWarpgroup());
            st.setString(9, warp.getAccessgroup());
            st.execute();
            rs = st.getGeneratedKeys();
            if(rs.next()) {
                warp.setId(rs.getInt(1));
            }

        } catch (Exception e) {
        	TweakWarp.log.warning("[TweakWarp]Error saving " + warp.toString() + ":");
            e.printStackTrace();
            return false;
        } finally {
        	try {
        		if(conn != null) conn.close();
        		if(st != null) st.close();
        		if(rs != null) rs.close();
        	} catch(Exception e) {}
        }
        return true;
    }

    public boolean deleteWarp(Warp warp) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
        	conn = getConnection();
            st = conn.prepareStatement(DELETE_WARP);
            st.setInt(1, warp.getId());
            st.execute();
        } catch (Exception e) {
        	TweakWarp.log.warning("[TweakWarp]Error deleting " + warp.toString() + ":");
            e.printStackTrace();
            return false;
        } finally {
        	try {
        		if(conn != null) conn.close();
        		if(st != null) st.close();
        		if(rs != null) rs.close();
        	} catch(Exception e) {}
        }
        return true;
    }
}
