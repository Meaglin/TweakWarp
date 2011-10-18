package com.guntherdw.bukkit.TweakWarp;

import org.bukkit.Location;
import org.bukkit.Server;

/**
 * @author GuntherDW
 */
public class Warp {
    

    private int id;
    private String name;
    private double x, y, z;
    private float pitch, yaw;
    private String world, warpgroup, accessgroup;

    /**
     *  Default constructor for persistence manager.
     */
	public Warp() {
		
	}
	
    public Warp(double x, double y, double z, float pitch, float yaw, String name, String world, String warpgroup, String accessgroup) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.name = name;
        this.world = world;
        this.warpgroup = warpgroup.toLowerCase();
        if(this.warpgroup.trim().equals("")) this.warpgroup = TweakWarp.DEFAULT_WARP_GROUP;
        this.accessgroup = accessgroup.toLowerCase();
    }
    
    public Warp(Location location, String name, String warpgroup, String accessgroup) {
    	this(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw(), name, location.getWorld().getName(), warpgroup, accessgroup);
    }

    public Location getLocation(Server server) {
    	return new Location(server.getWorld(getWorld()),getX(), getY() + 1, getZ(), getYaw(), getPitch());
    }
    
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public String getWorld() {
		return world;
	}

	public String getWarpgroup() {
		return warpgroup;
	}

	public String getAccessgroup() {
		return accessgroup;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public void setWarpgroup(String warpgroup) {
		this.warpgroup = warpgroup;
	}

	public void setAccessgroup(String accessgroup) {
		this.accessgroup = accessgroup;
	}
	
	@Override
	public String toString() {
		return "Warp{id:"+getId()+"name:"+getName()+" group:" + getWarpgroup() + " accessgroup:" + getAccessgroup() + " x:"+getX()+"y:"+getY()+"z:"+getZ()+"pitch:"+getPitch()+"yaw:"+getYaw()+"}";
	}

	public void setLocation(Location location) {
		setX(location.getX());
		setY(location.getY());
		setZ(location.getZ());
		setPitch(location.getPitch()); 
		setYaw(location.getYaw());
		setWorld(location.getWorld().getName());
	}
}
