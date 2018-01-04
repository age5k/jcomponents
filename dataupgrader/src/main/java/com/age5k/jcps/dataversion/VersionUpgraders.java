package com.age5k.jcps.dataversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class VersionUpgraders<T> {

	private static final Logger LOG = LoggerFactory.getLogger(VersionUpgraders.class);

	protected Map<String, VersionUpgraders<T>> map = new HashMap<>();

	protected List<VersionUpgrader<T>> upgraderList = new ArrayList<>();

	// the target data version to be upgraded to.
	private DataVersion targetVersion;

	private DataVersion currentVersion;

	public VersionUpgraders(DataVersion targetVersion) {
		this.targetVersion = targetVersion;
	}

	public void add(VersionUpgrader<T> vp) {
		this.upgraderList.add(vp);
	}

	public void upgrade(T ctx) {

		this.currentVersion = resolveDataVersion(ctx);

		LOG.info("dataVersion:" + currentVersion + ",targetVersion:" + this.targetVersion);
		while (true) {
			if (this.currentVersion == this.targetVersion) {
				// upgrade complete
				break;
			}

			DataVersion pre = this.currentVersion;
			DataVersion dv = this.tryUpgrade(ctx);
			if (dv == null) {
				LOG.warn("cannot upgrade from:" + pre + " to target:" + this.targetVersion);
				break;
			}
			LOG.info("successfuly upgrade from:" + pre + " to target:" + dv);
		}

	}

	private DataVersion tryUpgrade(T ctx) {
		DataVersion rt = null;
		for (VersionUpgrader<T> up : this.upgraderList) {
			if (this.currentVersion == up.getSourceVersion()) {
				up.upgrade(ctx);//
				rt = up.getTargetVersion();
				this.writeDataVersion(ctx, rt);
			}
		}
		if (rt != null) {
			this.currentVersion = rt;
		}
		return rt;
	}

	protected abstract void writeDataVersion(T ctx, DataVersion dver);

	protected abstract DataVersion resolveDataVersion(T ctx);

}
