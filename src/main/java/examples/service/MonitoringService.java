package examples.service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springinfra.service.BaseService;

@Slf4j
@Getter
@Service
public class MonitoringService extends BaseService {

//    private final Counter counter;

    public MonitoringService(MeterRegistry registry) {
//        this.counter = registry.counter("name");
    }
}
