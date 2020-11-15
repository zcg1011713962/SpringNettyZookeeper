package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * springIOC扫描
 */
@Configuration
@ComponentScan(basePackages = "context")
public class ComponentScanConfig {
}
