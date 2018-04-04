package no.dict.threads;

public abstract class GroupItemThread extends AbstractThread {

	private GroupThread parent;

	public GroupItemThread(GroupThread parent) {
		this.parent = parent;
		if (parent != null)
			parent.addThread(this);
	}

	public GroupThread getParent() {
		return parent;
	}

	public void removeFromGroup() {
		if (parent != null)
			parent.removeThread(this);
	}

	public void run() {
		try {
			execute();
		} catch (Exception e) {
		} finally {
			if (parent != null)
				parent.removeThread(this);
		}
	}

	public abstract void execute();

}
