package com.ntnikka.modules.pay.aliPay.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @ClassName PollingUtil
 * @Author Liuq
 * @Description todo
 * @Date 2018/12/4 17:11
 **/
public class PollingUtil {

    /**
     * 根据传入大小 随机返回下标
     * @param size
     * @return
     */
    public static int RandomIndex(int size){
        Random random = new Random();
        return random.nextInt(size);
    }

    /**
     * 权重随机数
     * @param weight [15,568,4181,2]
     * @return 索引值
     */
    public static int RandomWeightIndex(List<Integer> weight){
        List<Integer> weightTmp = new ArrayList<>(weight.size()+1);
        weightTmp.add(0);
        Integer sum = 0;
        for( Integer d : weight ){
            sum += d;
            weightTmp.add(sum);
        }
        Random random = new Random();
        int rand = random.nextInt(sum);
        int index = 0;
        for(int i = weightTmp.size()-1; i >0; i--){
            if( rand >= weightTmp.get(i)){
                index = i;
                break;
            }
        }
        return index;
    }

    public static void main(String[] args) {
        List<Integer> weightList = new ArrayList<>();
        weightList.add(5);
        weightList.add(2);
        weightList.add(3);
        int count0 = 0;
        int count1 = 0;
        int count2 = 0;
        for (int i = 0 ; i < 100 ; i++){
           switch (RandomWeightIndex(weightList)){
               case 0 :
                   count0++;
                   break;
               case 1 :
                   count1++;
                   break;
               case 2 :
                   count2++;
                   break;
           }
        }
        System.out.println("0 = " + count0);
        System.out.println("1 = " + count1);
        System.out.println("2 = " + count2);
    }
}
