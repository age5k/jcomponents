package com.age5k.jcps.framework.container;

import com.age5k.jcps.framework.provider.Provider;

/**
 * 
 * @author Wu
 *
 */

public interface Container {

	public static interface Aware {
		public void setContainer(Container app);
	}

	public void destroy();

	/**
	 * Create a object, set the container if the object support Aware interface.
	 * 
	 * @param cls
	 * @return
	 */
	public <T> T newInstance(Class<T> cls);

	/**
	 * 
	 * @param cls1
	 * @param cls2
	 * @return
	 */
	public <I, T extends I> T addComponent(Class<I> cls1, Class<T> impcls);

	/**
	 * Add component with the type declared.
	 * 
	 * @param cls
	 * @param obj
	 * @return
	 */
	public <T> Container addComponent(Class<T> cls, T obj);

	/**
	 * Find a component that support the type.
	 * 
	 * @param cls
	 * @param force
	 * @return
	 */
	public <T> T findComponent(Class<T> cls, boolean force);

	/**
	 * Instead of find the component now, this method will return a provider that
	 * will call findCommponent later: when the method Provider.get() is called.
	 * 
	 * @param cls
	 * @param force
	 * @return
	 */
	public <T> Provider<T> findComponentLater(Class<T> cls, boolean force);

}
