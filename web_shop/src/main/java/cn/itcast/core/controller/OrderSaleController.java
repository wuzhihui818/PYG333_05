package cn.itcast.core.controller;

import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/ordersale")
public class OrderSaleController {

    @Reference
    private OrderService orderService;

    @RequestMapping("/getData")
    public Map<String, List> guFindData(String start, String end) {
        Map<String, List> map = new HashMap<String, List>();
        //获取商家登录id
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        //查询销售额信息
        if (sellerId != null) {

            Map<String, Double> saleDataMap = orderService.findSaleData(start, end, sellerId);
            //将map按照key排序

            saleDataMap = sortMapByKey1(saleDataMap, true);

            //用来封装日期
            ArrayList<String> xAxisList = new ArrayList<>();
            //用来封装销售额
            ArrayList<Double> seriesSaleList = new ArrayList<>();
            //遍历数据，封装到新的map中
            Set<String> keySet = saleDataMap.keySet();
            for (String key : keySet) {
                //添加日期
                xAxisList.add(key);
                //添加销售额
                seriesSaleList.add(saleDataMap.get(key));
            }
            map.put("xAxisList", xAxisList);
            map.put("seriesSaleList", seriesSaleList);

            return map;
        }
        return null;
    }

    //将map按照key进行排序
    public static Map<String, Double> sortMapByKey1(Map<String, Double> oriMap, final boolean isRise) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Double> sortMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (isRise) {
                    // 升序排序
                    return o1.compareTo(o2);
                } else {
                    // 降序排序
                    return o2.compareTo(o1);
                }
            }
        });
        sortMap.putAll(oriMap);
        return sortMap;
    }

}