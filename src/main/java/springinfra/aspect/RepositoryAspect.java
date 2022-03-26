package springinfra.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import springinfra.database.repository.BaseRepositoryImpl;
import springinfra.model.domain.BaseDomain;

import java.util.List;
import java.util.Optional;

@Component
@Aspect
@Slf4j
public class RepositoryAspect extends BaseAspect {

    @AfterReturning(value = "this(org.springframework.data.repository.Repository)", returning = "returnValue")
    public void detachEntity(JoinPoint joinPoint, Object returnValue) throws Exception {
        if (returnValue == null) {
            return;
        }

        if (!AopUtils.isAopProxy(joinPoint.getTarget())) {
            log.warn("JoinPoint object isn't an instance of AOP proxy, so the detach process interrupted.");
            return;
        }

        BaseRepositoryImpl baseRepository;
        Object joinPointRealObject = ((Advised) joinPoint.getTarget()).getTargetSource().getTarget();
        if (joinPointRealObject instanceof BaseRepositoryImpl) {
            baseRepository = ((BaseRepositoryImpl) joinPointRealObject);
        } else {
            log.warn("JoinPoint real object isn't an instance of BaseRepositoryImpl class, so the detach process interrupted.");
            return;
        }

        if (returnValue instanceof BaseDomain baseDomain) {
            baseRepository.detach(baseDomain);

        } else if (returnValue instanceof List list) {
            for (Object item : list) {
                if (item instanceof BaseDomain baseDomain) {
                    baseRepository.detach(baseDomain);
                }
            }

        } else if (returnValue instanceof Optional optional) {
            optional.ifPresent(item -> {
                if (item instanceof BaseDomain baseDomain) {
                    baseRepository.detach(baseDomain);
                }
            });

        } else if (returnValue instanceof Page page) {
            page.getContent().forEach(item -> {
                if (item instanceof BaseDomain baseDomain) {
                    baseRepository.detach(baseDomain);
                }
            });
        }
    }
}
