package in.recallradar;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix="app") public record AppProperties(String corsOrigin,String setupCode,String jwtSecret,long accessMinutes,long refreshDays) {}
