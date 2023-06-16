package ${global.packageName}.${serviceImpl.packageName};

import com.mybatiseasy.core.base.IMapper;
import com.mybatiseasy.core.base.IService;

import org.springframework.beans.factory.annotation.Autowired;

public class BaseServiceImpl<M extends IMapper<T>, T> implements IService<T> {

    @Autowired
    protected M baseMapper;

    @Override
    public M getBaseMapper(){
        return this.baseMapper;
    }
}