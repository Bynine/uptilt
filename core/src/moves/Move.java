package moves;

import entities.Fighter;
import timers.Timer;

public abstract class Move {
	final Fighter user;
	final Timer duration;
	final ActionList actionList = new ActionList();
	boolean causesHelpless = false;
	boolean special = false;

	Move(Fighter user, int dur){
		this.user = user;
		duration = new Timer(dur, true);
	}

	public void update(){
		duration.countUp();
		actionList.update(duration.getCounter());
	}

	public boolean done(){ return duration.timeUp(); }
	public boolean causesHelpless() { return causesHelpless; }
	public int getDuration() { return duration.getEndTime(); }
	public boolean isSpecial() { return special; }
	// abstract Animation getAnimation();

	// MOVE LIST!

	// GROUNDED

	public static class DashAttack extends Move{
		public DashAttack(Fighter user) {
			super(user, 24);
			actionList.addVelocityChange(user, 2, user.direct() * 10, Action.ChangeVelocity.noChange); // TODO: change to constant velocity?
			Hitbox early = new Hitbox(user, 3, 2, 4, Hitbox.SAMURAIANGLE, 12, 0, 18);
			Hitbox late = new Hitbox(user, 3, 1, 3, 80, 18, 0, 12);
			late.connect(early);
			actionList.addActionCircle(early, 4, 12);
			actionList.addActionCircle(late, 13, 20);
		}
	}

	public static class UpTilt extends Move{
		public UpTilt(Fighter user) {
			super(user, 30);
			Hitbox h1 = new Hitbox(user, 2, 0, 1, 80, 6, -8, 6);
			Hitbox h2 = new Hitbox(user, 2, 0, 2, 90, 10, 0, 8);
			Hitbox h3 = new Hitbox(user, 4, 4, 4, 85, 10, 12, 10);
			actionList.addActionCircle(h1, 0, 10);
			actionList.addActionCircle(h2, 10, 20);
			actionList.addActionCircle(h3, 20, 30);
		}
	}

	public static class DownTilt extends Move{
		public DownTilt(Fighter user) {
			super(user, 20);
			Hitbox early = new Hitbox(user, 4, 2, 6, 80, 16, -12, 16);
			Hitbox late = new Hitbox(user, 3, 1, 5, 70, 16, -12, 14);
			late.connect(early);
			actionList.addActionCircle(early, 6, 8);
			actionList.addActionCircle(late, 9, 12);
		}
	}

	public static class ForwardTilt extends Move{
		public ForwardTilt(Fighter user) {
			super(user, 24);
			Hitbox early = new Hitbox(user, 2, 3, 7, Hitbox.SAMURAIANGLE, 16, 0, 16);
			Hitbox late = new Hitbox(user, 2, 2, 6, Hitbox.SAMURAIANGLE, 18, 0, 12);
			late.connect(early);
			actionList.addActionCircle(early, 6, 8);
			actionList.addActionCircle(late, 9, 12);
		}
	}

	public static class Jab extends Move{
		public Jab(Fighter user) {
			super(user, 12);
			Hitbox h1 = new Hitbox(user, 1, 1, 3, Hitbox.SAMURAIANGLE, 12, 0, 8);
			actionList.addActionCircle(h1, 4, 7);
		}
	}

	// AERIALS

	public static class Uair extends Move{
		public Uair(Fighter user) {
			super(user, 30);
			Hitbox early = new Hitbox(user, 4, 3, 7, 90, 0, 12, 15);
			Hitbox late = new Hitbox(user, 3, 2, 5, 80, 0, 12, 12);
			late.connect(early);
			actionList.addActionCircle(early, 5, 15);
			actionList.addActionCircle(late, 15, 25);
		}
	}

	public static class Dair extends Move{
		public Dair(Fighter user) {
			super(user, 30);
			Hitbox h1 = new Hitbox(user, 3, 3, 10, 270, 0, -12, 15);
			actionList.addActionCircle(h1, 10, 14);
		}
	}

	public static class Fair extends Move{
		public Fair(Fighter user) {
			super(user, 30);
			Hitbox early = new Hitbox(user, 4, 4, 7, 30, 12, 0, 20);
			Hitbox late = new Hitbox(user, 3, 3, 4, 50, 12, 0, 15);
			late.connect(early);
			actionList.addActionCircle(early, 8, 12);
			actionList.addActionCircle(late, 13, 16);
		}
	}

	public static class Bair extends Move{
		public Bair(Fighter user) {
			super(user, 30);
			Hitbox early = new Hitbox(user, 3, 3, 8, 60, -12, 0, 20);
			Hitbox late = new Hitbox(user, 2, 2, 5, 50, -12, 0, 15);
			late.connect(early);
			actionList.addActionCircle(early, 5, 15);
			actionList.addActionCircle(late, 15, 25);
		}
	}

	public static class Nair extends Move{
		public Nair(Fighter user) {
			super(user, 30);
			Hitbox early = new Hitbox(user, 3, 3, 7, 60, 0, 0, 20);
			Hitbox late = new Hitbox(user, 2, 2, 4, 50, 0, 0, 15);
			late.connect(early);
			actionList.addActionCircle(early, 5, 15);
			actionList.addActionCircle(late, 15, 25);
		}
	}

	// SPECIALS

	public static class UpSpecial extends Move{
		public UpSpecial(Fighter user) {
			super(user, 30);
			special = true;
			actionList.addVelocityChange(user, 0, 0, 1);
			actionList.addVelocityChange(user, 8, Action.ChangeVelocity.noChange, 12);
			Hitbox h1 = new Hitbox(user, 9, 0, 2, 90, 0, 5, 15);
			Hitbox h2 = new Hitbox(user, 6, 0, 2, 90, 0, 10, 10);
			Hitbox h3 = new Hitbox(user, 4, 4, 4, 80, 0, 12, 15);
			actionList.addActionCircle(h1, 5, 15);
			actionList.addActionCircle(h2, 15, 25);
			actionList.addActionCircle(h3, 25, 28);
			causesHelpless = true;
		}
	}

	public static class DownSpecial extends Move{
		public DownSpecial(Fighter user) {
			super(user, 40);
			special = true;
			actionList.addConstantVelocity(user, 8, 36, user.direct() * 2, -12);
			Hitbox h1 = new Hitbox(user, 4, 5, 14, 285, 12, -12, 20);
			h1.setProperty(Hitbox.Property.ELECTRIC);
			actionList.addActionCircle(h1, 8, 32);
		}
	}

	public static class SideSpecial extends Move{
		public SideSpecial(Fighter user) {
			super(user, 30);
			special = true;
			Hitbox h1 = new Hitbox(user, 4, 3, 3, 70, 8, 0, 6);
			Hitbox h2 = new Hitbox(user, 5, 5, 5, 40, 14, 0, 6);
			Hitbox h3 = new Hitbox(user, 5, 7, 7, 20, 20, 0, 6);
			h1.connect(h2);
			h1.connect(h3);
			h2.connect(h3);
			actionList.addActionCircle(h1, 12, 16);
			actionList.addActionCircle(h2, 12, 16);
			actionList.addActionCircle(h3, 12, 16);
		}
	}

	public static class NeutralSpecial extends Move{
		public NeutralSpecial(Fighter user) {
			super(user, 60);
			special = true;
			actionList.addVelocityChange(user, 32, user.direct() * 7, Action.ChangeVelocity.noChange);
			Hitbox h1 = new Hitbox(user, 7, 5, 20, 30, 10, 0, 25);
			actionList.addActionCircle(h1, 34, 42);
		}
	}

	public static class Grab extends Move{
		public Grab(Fighter user) {
			super(user, 30);
			Grabbox g1 = new Grabbox(user, 8, 0, 10);
			actionList.addActionCircle(g1, 7, 10);
		}
	}

	public static class FThrow extends Move{ public FThrow(Fighter user){
		super(user, 15);
		Hitbox h1 = new Hitbox(user, 5, 3, 5, 30, 16, 0, 20);
		actionList.addActionCircle(h1, 0, 1);
	} }
	
	public static class BThrow extends Move{ public BThrow(Fighter user){
		super(user, 15);
		Hitbox h1 = new Hitbox(user, 3, 5, 5, 140, 16, 0, 20);
		actionList.addActionCircle(h1, 0, 1);
	} }
	
	public static class UThrow extends Move{ public UThrow(Fighter user){
		super(user, 15);
		Hitbox h1 = new Hitbox(user, 4, 3, 5, 90, 16, 0, 20);
		actionList.addActionCircle(h1, 0, 1);
	} }
	
	public static class DThrow extends Move{ public DThrow(Fighter user){
		super(user, 15);
		Hitbox h1 = new Hitbox(user, 3, 1, 4, 75, 16, 0, 20);
		actionList.addActionCircle(h1, 0, 1);
	} }

}
