package moves;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.UptiltEngine;
import moves.Hitbox.Property;
import timers.DurationTimer;
import timers.Timer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import entities.Fighter;
import entities.Fighter.HitstunType;

public abstract class ActionCircle {

	final Fighter user;
	final float dispX, dispY;
	final Circle area;
	final Timer duration = new DurationTimer(5);
	final Timer refreshTimer = new DurationTimer(6);
	private boolean initialHit;
	boolean remove = false;
	boolean doesRefresh = false;
	final List<Fighter> hitFighterList = new ArrayList<Fighter>();
	private Set<Fighter> s;
	Property property = Property.NORMAL;
	Fighter.HitstunType hitstunType = HitstunType.NORMAL;
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
		if (null != group) for (ActionCircle ac: group.connectedCircles) {
			hitFighterList.addAll(ac.hitFighterList);
			s = new HashSet<Fighter>(hitFighterList);
			hitFighterList.clear();
			hitFighterList.addAll(s);
			if (ac.isInitialHit() || ac.remove) remove = true;
		}
	}

	public void setDuration(int dur){
		duration.setEndTime(dur);
	}

	public void update(int deltaTime){
		checkGroup();
		duration.countUp();
		if (UptiltEngine.outOfHitlag()){
			area.x = getX();
			area.y = getY();
			if (isInitialHit()) remove = true;
		}
	}

	public void updatePosition() {
		area.setX(getX());
		area.setY(getY());
	}

	public void reset(){
		hitFighterList.clear();
		remove = false;
		setInitialHit(false);
	}

	float getX(){ return user.getPosition().x + user.getImage().getWidth()/2 + (user.direct() * dispX); }
	float getY(){ return user.getPosition().y + user.getImage().getHeight()/2 + dispY; }
	public Circle getArea(){ return area; }
	public boolean toRemove() { return duration.timeUp(); }
	boolean didHitTarget(Fighter target){ 
		return user.getTeam() != target.getTeam() && !remove && !user.attackTimer.timeUp() 
				&& !target.isInvincible() && Intersector.overlaps(area, target.getHurtBox()) && !hitFighterList.contains(target); 
	}
	public void setRefresh(int time) { 
		refreshTimer.setEndTime(time);
		doesRefresh = true; 
	}

	public boolean isInitialHit() {
		return initialHit;
	}

	public void setInitialHit(boolean initialHit) {
		this.initialHit = initialHit;
	}

	public boolean hitAnybody() {
		return hitFighterList.size() >= 1;
	}
	
	protected Object clone() throws CloneNotSupportedException { return super.clone(); }

}
