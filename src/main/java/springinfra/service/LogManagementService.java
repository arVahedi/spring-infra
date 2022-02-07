package springinfra.service;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.stereotype.Service;

@Service
public class LogManagementService extends BaseService {

    public void setLoggerLevel(String name, String level) {
        Level log4jLevel = Level.valueOf(level);
        if (name.equalsIgnoreCase("ROOT")) {
            Configurator.setRootLevel(log4jLevel);
        } else {
            Configurator.setAllLevels(name, log4jLevel);
        }
    }

    public Level getLoggerLevel(String name) {
        return LogManager.getLogger(name).getLevel();
    }
}
