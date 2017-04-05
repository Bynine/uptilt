package moves;

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
	Timer duration = new Timer(5, true);
	boolean hit;
	Property property = Property.NORMAL;
	ActionCircle connectedCircle = null;

	public ActionCircle(Fighter user, float dispX, float dispY, int size){
		this.user = user;
		this.dispX = dispX;
		this.dispY = dispY;
		area = new Circle(user.getPosition().x + user.getHurtBox().getWidth()/2 + (user.direct() * dispX), 
				user.getPosition().y + user.getHurtBox().getHeight()/2 + dispY, size);
	}
	
	public abstract void hitTarget(Fighter target);
	public abstract Color getColor();
	
	public void connect(ActionCircle ac) { connectedCircle = ac; }
	public void checkConnected(){ if (null != connectedCircle && connectedCircle.hit) hit = true; }
	
	public void setDuration(int dur){
		duration.setEndTime(dur);
	}

	public void update(){
		duration.countUp();
		area.x += user.getVelocity().x;
		area.y += user.getVelocity().y;
	}

	public void updatePosition() {
		area.setX(user.getPosition().x + user.getHurtBox().getWidth()/2 + (user.direct() * dispX));
		area.setY(user.getPosition().y + user.getHurtBox().getHeight()/2 + dispY);
	}
	
	public Circle getArea(){ return area; }
	public boolean toRemove() { return duration.timeUp() || hit; }
	boolean didHitTarget(Fighter target){ return target != user && !target.isInvincible() && Intersector.overlaps(area, target.getHurtBox()); }
	
}
