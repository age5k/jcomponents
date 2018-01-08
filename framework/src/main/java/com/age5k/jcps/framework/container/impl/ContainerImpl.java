package com.age5k.jcps.framework.container.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.container.Container.Aware;
import com.age5k.jcps.framework.provider.Provider;

public class ContainerImpl implements Container {
	private static final Logger LOG = LoggerFactory.getLogger(ContainerImpl.class);

	private Map<Class, Object> componentMap = new HashMap<>();

	public ContainerImpl() {

		// this.dbTemplate = new GDBTemplate();
	}

	public void destroy() {

	}
	@Override
	public <T> T addComponent(Class<T> cls) {
		return this.addComponent(cls, cls);
	}
	@Override
	public <I, T extends I> T addComponent(Class<I> itf, Class<T> cls) {
		T rt = newInstance(cls);
		this.addComponent(itf, rt);
		return rt;
	}

	@Override
	public <T> T newInstance(Class<T> cls) {
		T rt;
		try {
			rt = cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw JcpsException.toRtException(e);
		}

		if (rt instanceof Container.Aware) {
			((Container.Aware) rt).setContainer(this);
		}
		return rt;
	}

	@Override
	public <T> T findComponent(Class<T> cls, boolean force) {
		Object rt = this.componentMap.get(cls);
		if (rt == null && force) {
			throw new JcpsException("no component found for cls:" + cls);
		}
		return (T) rt;
	}

	@Override
	public <T> Container addComponent(Class<T> cls, T obj) {
		Object old = this.componentMap.put(cls, obj);
		if (old != null) {
			throw new JcpsException("duplicated component:" + old);
		}
		return this;
	}

	@Override
	public <T> Provider<T> findComponentLater(Class<T> cls, boolean force) {

		return new Provider<T>() {
			private T target;
			private boolean got;

			@Override
			public T get() {
				if (!this.got) {
					this.target = ContainerImpl.this.findComponent(cls, false);
				}
				if (force && this.target == null) {
					throw new JcpsException("no component found for cls:" + cls);
				}
				return this.target;
			}
		};
	}

}
