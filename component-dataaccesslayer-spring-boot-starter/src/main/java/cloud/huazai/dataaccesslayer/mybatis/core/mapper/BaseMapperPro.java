package cloud.huazai.dataaccesslayer.mybatis.core.mapper;

import cloud.huazai.datatransmission.request.PageQuery;
import cloud.huazai.datatransmission.request.SortField;
import cloud.huazai.datatransmission.response.ResponseResult;
import cloud.huazai.dataaccesslayer.mybatis.core.util.JdbcUtils;
import cloud.huazai.tool.ResponseResultTool;
import cloud.huazai.tool.java.lang.StringUtils;
import cloud.huazai.tool.java.util.CollectionUtils;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 在 MyBatis Plus 的 BaseMapper 的基础上拓展，提供更多的能力
 *
 * 1. {@link com.baomidou.mybatisplus.core.mapper.BaseMapper} 为 MyBatis Plus 的基础接口，提供基础的 CRUD 能力
 * 2. {@link MPJBaseMapper} 为 MyBatis Plus Join 的基础接口，提供连表 Join 能力
 */
public interface BaseMapperPro<T> extends MPJBaseMapper<T> {

    default ResponseResult<T> selectPage(PageQuery pageParam, @Param("ew") Wrapper<T> queryWrapper) {
        Page<T> queryPage = new Page<>(pageParam.getPageNo(), pageParam.getPageSize());
        // 排序字段
        if (CollectionUtils.isNotEmpty(pageParam.getSortFieldList())) {
            queryPage.addOrder(pageParam.getSortFieldList().stream().map(sortField -> SortField.ORDER_ASC.equals(sortField.getOrder())
                            ? OrderItem.asc(StringUtils.toUnderlineCase(sortField.getField()))
                            : OrderItem.desc(StringUtils.toUnderlineCase(sortField.getField())))
                    .collect(Collectors.toList()));
        }


        IPage<T> resultPage = selectPage(queryPage, queryWrapper);
        // 转换返回
        return ResponseResultTool.buildSuccess(resultPage.getRecords(), resultPage.getTotal(), resultPage.getSize(), resultPage.getCurrent());
    }

    default ResponseResult<T> selectPage(PageQuery pageParam, LambdaQueryWrapper<T> lambdaQueryWrapper) {

        Page<T> queryPage = new Page<>(pageParam.getPageNo(), pageParam.getPageSize());
        // 排序字段
        if (CollectionUtils.isNotEmpty(pageParam.getSortFieldList())) {
            queryPage.addOrder(pageParam.getSortFieldList().stream().map(sortField -> SortField.ORDER_ASC.equals(sortField.getOrder())
                            ? OrderItem.asc(StringUtils.toUnderlineCase(sortField.getField()))
                            : OrderItem.desc(StringUtils.toUnderlineCase(sortField.getField())))
                    .collect(Collectors.toList()));
        }

        IPage<T> resultPage = selectPage(queryPage, lambdaQueryWrapper);
        // 转换返回
        return ResponseResultTool.buildSuccess(resultPage.getRecords(), resultPage.getTotal(), resultPage.getSize(), resultPage.getCurrent());
    }


    default <D> ResponseResult<D> selectJoinPage(PageQuery pageParam, Class<D> clazz, MPJLambdaWrapper<T> lambdaWrapper) {
        Page<D> queryPage = new Page<>(pageParam.getPageNo(), pageParam.getPageSize());

        // 排序字段
        if (CollectionUtils.isNotEmpty(pageParam.getSortFieldList())) {
            queryPage.addOrder(pageParam.getSortFieldList().stream().map(sortField -> SortField.ORDER_ASC.equals(sortField.getOrder())
                            ? OrderItem.asc(StringUtils.toUnderlineCase(sortField.getField()))
                            : OrderItem.desc(StringUtils.toUnderlineCase(sortField.getField())))
                    .collect(Collectors.toList()));
        }

        IPage<D> resultPage = selectJoinPage(queryPage, clazz, lambdaWrapper);
        // 转换返回
        return ResponseResultTool.buildSuccess(resultPage.getRecords(), resultPage.getTotal(), resultPage.getSize(), resultPage.getCurrent());
    }

    default <DTO> ResponseResult<DTO> selectJoinPage(PageQuery pageParam, Class<DTO> resultTypeClass, MPJBaseJoin<T> joinQueryWrapper) {
        Page<DTO> queryPage = new Page<>(pageParam.getPageNo(), pageParam.getPageSize());

        // 排序字段
        if (CollectionUtils.isNotEmpty(pageParam.getSortFieldList())) {
            queryPage.addOrder(pageParam.getSortFieldList().stream().map(sortField -> SortField.ORDER_ASC.equals(sortField.getOrder())
                            ? OrderItem.asc(StringUtils.toUnderlineCase(sortField.getField()))
                            : OrderItem.desc(StringUtils.toUnderlineCase(sortField.getField())))
                    .collect(Collectors.toList()));
        }

        IPage<DTO> resultPage =  selectJoinPage(queryPage, resultTypeClass, joinQueryWrapper);
        // 转换返回
        return ResponseResultTool.buildSuccess(resultPage.getRecords(), resultPage.getTotal(), resultPage.getSize(), resultPage.getCurrent());
    }


    /**
     * 批量插入，适合大量数据插入
     *
     * @param entities 实体们
     */
    default Boolean insertBatch(Collection<T> entities) {
        // 特殊：SQL Server 批量插入后，获取 id 会报错，因此通过循环处理
        DbType dbType = JdbcUtils.getDbType();
        if (JdbcUtils.isSQLServer(dbType)) {
            entities.forEach(this::insert);
            return CollectionUtils.isNotEmpty(entities);
        }
        return Db.saveBatch(entities);
    }

    /**
     * 批量插入，适合大量数据插入
     *
     * @param entities 实体们
     * @param size     插入数量 Db.saveBatch 默认为 1000
     */
    default Boolean insertBatch(Collection<T> entities, int size) {
        // 特殊：SQL Server 批量插入后，获取 id 会报错，因此通过循环处理
        DbType dbType = JdbcUtils.getDbType();
        if (JdbcUtils.isSQLServer(dbType)) {
            entities.forEach(this::insert);
            return CollectionUtils.isNotEmpty(entities);
        }
        return Db.saveBatch(entities, size);
    }

    default int update(T entity) {
        return updateById(entity);
    }

    default Boolean updateBatch(Collection<T> entities) {
        return Db.updateBatchById(entities);
    }

    default Boolean updateBatch(Collection<T> entities, int size) {
        return Db.updateBatchById(entities, size);
    }

    default int delete(T entity) {
        return deleteById(entity);
    }


    default int deleteBatch(Collection<T> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return 0;
        }
        return deleteByIds(entities);
    }

}
