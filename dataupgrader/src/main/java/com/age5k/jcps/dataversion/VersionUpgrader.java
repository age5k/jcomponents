package com.age5k.jcps.dataversion;
public abstract class VersionUpgrader<T> {

	protected DataVersion sourceVersion;
	protected DataVersion targetVersion;

	public VersionUpgrader(DataVersion source, DataVersion target) {
		this.sourceVersion = source;
		this.targetVersion = target;
	}

	public DataVersion getTargetVersion() {
		return this.targetVersion;
	}

	public void upgrade(T ctx) {

		doUpgrade(ctx);
		doAfterUpgrade(ctx);

	}

	/**
	 * Upgrade version info .
	 * 
	 * @param con
	 * @param t
	 */
	protected void doAfterUpgrade(T ctx) {
		
	}
	
	public abstract void doUpgrade(T ctx);

	public DataVersion getSourceVersion() {
		return sourceVersion;
	}

}
