package com.zeitheron.hammercore.utils;

public class FrictionRotator
{
	public float degree;
	public float prevDegree;
	
	public float friction = 1;
	
	public float currentSpeed;
	public float speed;
	
	public void speedup(float speed)
	{
		this.speed += speed;
	}
	
	public void slowdown(float speed)
	{
		this.speed -= speed;
	}
	
	public float getActualRotation(float partialTime)
	{
		return (prevDegree + (degree - prevDegree) * partialTime) % 360;
	}
	
	public void update()
	{
		if(Float.isNaN(speed))
			speed = 0;
		if(Float.isNaN(currentSpeed))
			currentSpeed = 0;
		
		if(currentSpeed > speed)
			currentSpeed -= Math.sqrt(currentSpeed - speed);
		else if(speed > currentSpeed)
			currentSpeed += Math.sqrt(speed - currentSpeed);
		if(Math.abs(currentSpeed) - friction <= 4.0E-3F)
			currentSpeed = 0;
		
		prevDegree = degree;
		degree += currentSpeed;
		
		float frictionBoost = (float) Math.sqrt(Math.sqrt(Math.abs(speed)));
		if(speed > 0)
			speed = Math.max(0, speed - friction * frictionBoost);
		else if(speed < 0)
			speed = Math.min(0, speed + friction * frictionBoost);
	}
}