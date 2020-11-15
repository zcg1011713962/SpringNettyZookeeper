package context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeansException;
import utils.LogUtil;

import java.util.Map;

/**
 * 自动调用setApplicationContext方法来为我们设置上下文实例
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LogUtil.info("-----------------------自动调用setApplicationContext方法来为我们设置上下文实例");
        this.applicationContext = applicationContext;
    }

    /**
     * 根据类型获取beans
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> clazz){
        return applicationContext.getBeansOfType(clazz);
    }
}
