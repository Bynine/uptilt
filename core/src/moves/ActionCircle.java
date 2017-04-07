package moves;

import main.UptiltEngine;
import moves.Hitbox.Property;
import timers.Timer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import entities.Fighter;

public abstract class ActionCircle {

	final Fighter user;
	final float dispX, dispY;
	final Circle area;
	final Timer duration = new Timer(5, true);
	final Timer refreshTimer = new Timer(6, true);
	boolean initialHit, remove = false;
	boolean doesRefresh = false;
	Property property = Property.NORMAL;
	ActionCircleGroup group = null;

	public ActionCircle(Fighter user, float dispX, float dispY, int size){
		this.user = user;
		this.dispX = dispX;
		this.dispY = dispY;
		area = new Circle(getX(), getY(), size);
	}

	public abstract void hitTarget(Fighter target);
	public abstract Color getColor();

	public void checkGroup(){ 
		if (null != group) for (ActionCircle ac: group.connectedCircles) if (ac.initialHit) remove = true;
	}

	public void setDuration(int dur){
		duration.setEndTime(dur);
	}

	public void update(int deltaTime){
		duration.countUp();
		if (UptiltEngine.outOfHitlag()){
			area.x += user.getVelocity().x;
			area.y += user.getVelocity().y;
			if (initialHit) remove = true;
		}
	}

	public void updatePosition() {
		area.setX(getX());
		area.setY(getY());
	}

	public void reset(){
		remove = false;
		initialHit = false;
	}

	private float getX(){ return user.getPosition().x + user.getHurtBox().getWidth()/2 + (user.direct() * dispX); }
	private float getY(){ return user.getPosition().y + user.getHurtBox().getHeight()/2 + dispY; }
	public Circle getArea(){ return area; }
	public boolean toRemove() { return duration.timeUp(); }
	boolean didHitTarget(Fighter target){ 
		return !remove && !user.attackTimer.timeUp() && target != user && !target.isInvincible() && Intersector.overlaps(area, target.getHurtBox()); 
	}
	public void setRefresh(int time) { 
		refreshTimer.setEndTime(time);
		doesRefresh = true; 
	}

}
