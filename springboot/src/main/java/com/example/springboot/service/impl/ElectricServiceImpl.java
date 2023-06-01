package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Electric;
import com.example.springboot.mapper.ElectricMapper;
import com.example.springboot.service.ElectricService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class ElectricServiceImpl extends ServiceImpl<ElectricMapper, Electric> implements ElectricService {

    /**
     * 注入DAO层对象
     */
    @Resource
    private ElectricMapper electricMapper;

    /**
     * 添加订单
     */
    @Override
    public int addNewOrder(Electric repair) {
        int insert = electricMapper.insert(repair);
        return insert;
    }

    /**
     * 查找订单
     */
    @Override
    public Page find(Integer pageNum, Integer pageSize, String search, String date1, String date2, Integer dormRoomId) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<Electric> qw = new QueryWrapper<>();
        if (dormRoomId != null){
            qw.eq("roomid",dormRoomId);
        }
        if (StringUtils.isNotEmpty(date1) && StringUtils.isNotEmpty(date2)) {

//            小于
            try {
                qw.le("date", convertDate(date2));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            大于
            try {
                qw.ge("date", convertDate(date1));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        qw.orderByDesc("date");
        Page orderPage = electricMapper.selectPage(page, qw);
        return orderPage;
    }

    @Override
    public Page individualFind(Integer pageNum, Integer pageSize, String search, String name) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<Electric> qw = new QueryWrapper<>();
        qw.like("operatorName", search);
        qw.orderByDesc("date");
        Page orderPage = electricMapper.selectPage(page, qw);
        return orderPage;
    }

    @Override
    public int deleteOrder(Integer id) {
        return 0;
    }

    public static String convertDate(String strDate) throws ParseException {
        //进行转化时区
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        Date myDate = dateFormat.parse(strDate.replace("Z", "+0000"));
        //转换为年月日时分秒
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = df.format(myDate);
        System.out.println(format);
        return format;
    }

}
