package tech.lancelot.repository.system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tech.lancelot.domain.system.DictDetail;

import java.util.List;

public interface DictDetailRepository extends JpaRepository<DictDetail, Long>, JpaSpecificationExecutor<DictDetail> {

    /**
     * 根据字典名称查询
     * @param name /
     * @return /
     */
    List<DictDetail> findByDictName(String name);
}
