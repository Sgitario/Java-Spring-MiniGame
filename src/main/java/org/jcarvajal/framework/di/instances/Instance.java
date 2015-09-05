package org.jcarvajal.framework.di.instances;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.jcarvajal.framework.utils.ReflectionUtils;
import org.jcarvajal.framework.utils.StringUtils;
import org.jcarvajal.framework.di.DependencyInjectorBase;
import org.jcarvajal.framework.di.annotations.Autowired;
import org.jcarvajal.framework.di.annotations.Init;
import org.jcarvajal.framework.di.exceptions.InstantiationException;

/**
 * The instance follows the prototype design pattern to create/clone 
 * instances within the dependency injector registry.
 * 
 * The instance prototype is responsible for:
 * - Create the instance.
 * - Resolve autowired fields.
 * - Execute init annotated methods.
 * 
 * The instance life cycle supports:
 * - Requested: When the instance is requested.
 * - Init: When the instance is registered.
 * 
 * @author jhilario
 *
 */
public abstract class Instance {
	
	private final String bindClassName;
	private final String implClassName;
	private final Map<String, String> params;
	private Object instance;
	private final DependencyInjectorBase injector;
	
	/**
	 * Initializes a new instance of the Instance class.
	 * @param injector
	 * @throws InstantiationException 
	 */
	public Instance(String bindClassName, String implClassName, Map<String, String> params, 
			DependencyInjectorBase injector) 
			throws InstantiationException {
		this.bindClassName = bindClassName;
		String implementedBy = implClassName;
		if (!StringUtils.isNotEmpty(implClassName)) {
			implementedBy = this.bindClassName;
		}
		
		this.implClassName = implementedBy;
		this.params = params;
		this.injector = injector;
		
		onInit();
	}
	
	/**
	 * @return the resolved instance.
	 */
	public Object instance() {
		onRequested();
		return instance;
	}
	
	/**
	 * Life cycle method: Invoked when the instance is requested.
	 */
	public void onRequested() {
		
	}
	
	/**
	 * Life cycle method: Invoked when the instance is initialized.
	 */
	public void onInit() throws InstantiationException {
		
	}
	
	/**
	 * Create the instance, then resolve the autowired fields and finally run the init methods.
	 * @throws InstantiationException
	 */
	protected void createInstance() throws InstantiationException {
		Class<?> bindClazz = ReflectionUtils.createClass(bindClassName);
		if (bindClazz == null) {
			throw new InstantiationException("Bind class name %s not found", bindClassName);
		}
		
		this.instance = ReflectionUtils.createInstanceSafely(implClassName, params, bindClazz);
		if (this.instance == null) {
			throw new InstantiationException("Instance %s cannot be created with %s", this.bindClassName, this.implClassName);
		}
		
		resolveAutowiredFields();
		runInitMethods();
	}
	
	/**
	 * Loop over the fields and try to set the values when the field is annotated with Autowired.
	 * @throws InstantiationException
	 */
	private void resolveAutowiredFields() throws InstantiationException {
		if (this.instance() != null) {
			Field[] fields = this.instance().getClass().getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Autowired.class)) {
					// Find
					Object value = this.injector.get(field.getType().getName());
					if (value == null) {
						throw new InstantiationException(
								"Field %s cannot be resolved for %s class. ", field.getName(), implClassName);
					}
					
					// Set
					try {
						ReflectionUtils.invokeSetField(this.instance(), field.getName(), value, field.getType());
					} catch (Exception e) {
						throw new InstantiationException(
								"Field %s has not set method or is not visible for %s class. ", field.getName(), implClassName);
					}
				}
			}
		}
	}
	
	/**
	 * Loop over the methods and try to set the values when the field is annotated with Init.
	 * @throws InstantiationException
	 */
	private void runInitMethods() throws InstantiationException {
		if (this.instance() != null) {
			Method[] methods = this.instance().getClass().getDeclaredMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(Init.class)) {					
					// Set
					try {
						ReflectionUtils.invokeMethod(this.instance(), method);
					} catch (Exception e) {
						throw new InstantiationException(
								"Method %s cannot be executed at init stage for %s class ", method.getName(), implClassName);
					}
				}
			}
		}
	}
}
