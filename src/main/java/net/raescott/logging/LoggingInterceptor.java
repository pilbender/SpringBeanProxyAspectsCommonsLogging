/*************************************************************
 LoggingInterceptor :

 Description :
     Logging Interceptor based on AOP.
 **************************************************************/
package net.raescott.logging;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

/**
 * <p>
 * The Logging Interceptor is based on Aspect-oriented programming
 * (AOP) feature provided by Spring Framework.
 * <p>
 * The AOP technique helps us to add design and run-time behavior to
 * an object model in a non-obtrusive manner by using static and
 * dynamic crosscutting. By using AOP, we can write code for a
 * crosscutting functionality and then apply it declaratively (via
 * annotations or XML declaration) to already existing code.
 * <p>
 * 
 * Logging basically has two notable characteristics that inspires us
 * to implement it as an Aspect:
 * <li>
 * Logging code is often duplicated across an application, leading to
 * a lot of redundant code across multiple components in the
 * application. Even if the logging logic is abstracted to a separate
 * module so that different components have a single method call, that
 * single method call is duplicated in multiple places.</li>
 * <li>
 * Logging logic does not provide any business functionality; it's not
 * related to the domain of a business application.</li>
 * <p>
 * Spring uses a proxy-based approach to tie aspects into the object
 * model. That is, Spring intercepts all method invocations to the
 * aspected objects and then proxies the method invocations to the
 * intended objects. So, an application makes a method call to a
 * business object, Spring intercepts that call, applies the weaved
 * crosscutting aspect (LoggingInterceptor), and then makes a method
 * call to the intended business object.
 * <p>
 * 
 * @author Amarendra Bhaskar
 */
public class LoggingInterceptor implements MethodBeforeAdvice,
        AfterReturningAdvice, ThrowsAdvice {
    
    private static Log logger = null;
    private static final String ENTERING_METHOD_INDICATOR = "Entering --> ";
    private static final String EXITING_METHOD_INDICATOR = "Exiting <-- ";
    private boolean enableDTODebug = false;
    
    public LoggingInterceptor() {
    }

    /**
     * Callback before a given method is invoked.
     * 
     * @param method
     *            method being invoked
     * @param args
     *            arguments to the method
     * @param target
     *            target of the method invocation. May be
     *            <code>null</code>.
     * 
     * @throws Throwable
     *             if this object wishes to abort the call. Any
     *             exception thrown will be returned to the caller if
     *             it's allowed by the method signature. Otherwise the
     *             exception will be wrapped as a runtime exception.
     */
	@Override
    public void before(Method method, Object[] args, Object target)
            throws Throwable {
        logger = LogFactory.getLog(target.getClass());

        if (logger.isDebugEnabled()) {
            String msg = ENTERING_METHOD_INDICATOR
                    + target.getClass().getSimpleName() + "."
                    + method.getName() + "(";

            for (int i = 0; i < args.length; i++) {
                if (i > 0) {
                    msg += ", ";
                }
                msg += (isDataTransferObject(args[i]) && enableDTODebug) ? reflectionToString(args[i])
                        : args[i];
            }
            msg += ")";
            logger.debug(msg);
        }

        if (logger.isInfoEnabled() && !logger.isDebugEnabled()) {
            logger.info(ENTERING_METHOD_INDICATOR
                    + target.getClass().getSimpleName() + "."
                    + method.getName() + "()");
        }
    }

    /**
     * Callback after a given method successfully returned.
     * 
     * @param returnValue
     *            the value returned by the method, if any
     * @param method
     *            method being invoked
     * @param args
     *            arguments to the method
     * @param target
     *            target of the method invocation. May be
     *            <code>null</code>.
     * @throws Throwable
     *             if this object wishes to abort the call. Any
     *             exception thrown will be returned to the caller if
     *             it's allowed by the method signature. Otherwise the
     *             exception will be wrapped as a runtime exception.
     */
	@Override
    public void afterReturning(Object returnValue, Method method,
            Object[] args, Object target) throws Throwable {
        logger = LogFactory.getLog(target.getClass());

        if (logger.isDebugEnabled()) {
            logger.debug(EXITING_METHOD_INDICATOR
                    + target.getClass().getSimpleName() + "."
                    + method.getName()
                    + "(): Returned = "
                    + ((isDataTransferObject(returnValue) && enableDTODebug) ? reflectionToString(returnValue)
                                    : returnValue));
        }

        if (logger.isInfoEnabled() && !logger.isDebugEnabled()) {
            logger.info(EXITING_METHOD_INDICATOR
                    + target.getClass().getSimpleName() + "."
                    + method.getName() + "()");
        }
    }

    /**
     * * This method is invoked by reflection.
     * 
     * @param method
     *            method being invoked
     * @param args
     *            arguments to the method
     * @param target
     *            target of the method invocation. May be
     *            <code>null</code>.
     * @param exception
     */
    public void afterThrowing(Method method, Object[] args, Object target,
            Throwable exception) {
        logger = LogFactory.getLog(target.getClass());
        if (logger.isDebugEnabled()) {
            logger.debug("Intercepted exception of type ["
                    + exception.getClass().getName()
                    + "] thrown by target class ["
                    + target.getClass().getName() + "] and method ["
                    + method.toString() + "]");
            logger.debug("Exception is: " + exception.getMessage());
        }

        if (logger.isInfoEnabled() && !logger.isDebugEnabled()) {
            logger.debug("Intercepted exception of type ["
                    + exception.getClass().getName()
                    + "] thrown by target class ["
                    + target.getClass().getName() + "] and method ["
                    + method.toString() + "]");
            logger.info("Exception is: " + method.getName() + "()");
        }

        if (logger.isErrorEnabled()) {
            logger.error("Intercepted exception of type ["
                    + exception.getClass().getName()
                    + "] thrown by target class ["
                    + target.getClass().getName() + "] and method ["
                    + method.toString() + "]");
            logger.error("Exception is: " + exception.getMessage());
        }
    }
    
    /**
     * This method is used for debugging purpose.Returns a String
     * containing all public method names and it's value.
     * <p>
     * 
     * @param obj
     *            Object under inspection
     * @return
     */
    private String reflectionToString(Object obj) {
        String objgraph = "";
        if (obj != null) {
            objgraph = obj.getClass().getSimpleName();
            objgraph += " {";
            Method[] methods = obj.getClass().getMethods();
            for (Method method : methods) {
                if (isGetter(method)) {
                    objgraph += method.getName().replaceFirst("get", "");
                    objgraph += "=<";
                    try {
                        objgraph += method.invoke(obj, (Object[]) null);
                    } catch (Exception e) {
                        // do nothing
                        // Will think later
                    }
                    objgraph += ">";
                }
            }
            objgraph += "}";
        }
        return objgraph;
    }

    /**
     * Helper method to identify whether the method is a getter
     * method?
     * 
     * @param method
     * @return
     */
    private boolean isGetter(Method method) {
        boolean isGetter = true;
        if (method != null) {
            if (!method.getName().startsWith("get")) {
                isGetter = false;
            }
            if (method.getParameterTypes().length != 0) {
                isGetter = false;
            }
            if (void.class.equals(method.getReturnType())) {
                isGetter = false;
            }
            if (method.getName().equals("getClass")) {
                isGetter = false;
            }
        }
        return isGetter;
    }

    /**
     * Helper method to identify whether the object is a DTO.
     * 
     * @param method
     * @return
     */
    private boolean isDataTransferObject(Object obj) {
        boolean isDTO = false;
        if ((obj != null) && (obj.getClass().getSimpleName().endsWith("DTO"))) {
            isDTO = true;
        }
        return isDTO;
    }

    /**
     * Setter method wired through spring
     * 
     * @param enableDTODebug
     */
    public void setEnableDTODebug(boolean enableDTODebug) {
        this.enableDTODebug = Boolean.valueOf(enableDTODebug);
    }
}
