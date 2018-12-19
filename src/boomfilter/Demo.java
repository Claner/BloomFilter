package boomfilter;

import com.alibaba.fastjson.JSONObject;
import org.apache.lucene.util.RamUsageEstimator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

public class Demo {
    public static void main(String[] args) throws Exception {

        //先读取一次，避免误差
        initHashSet();

        System.out.println("-----------------");

        HashSet<String> hashSet = initHashSet();
        long start = System.currentTimeMillis();
        boolean hFlag = hashSet.contains("ffff939163d74b79a9c2eb6c2940fa3d");
        System.out.println(hFlag + "---hashSet判断是否包含元素耗时：" + (System.currentTimeMillis() - start) + "ms");

        System.out.println("-----------------");

        BloomFilter filter = initBloomFilter();
        start = System.currentTimeMillis();
        boolean fFlag = filter.contains("ffff939163d74b79a9c2eb6c2940fa3d");
        System.out.println(fFlag + "----过滤器判断是否包含元素耗时：" + (System.currentTimeMillis() - start) + "ms");

        System.out.println("-----------------");

        System.out.println("HashSet:" + RamUsageEstimator.humanSizeOf(hashSet));
        System.out.println("BloomFilter:" + RamUsageEstimator.humanSizeOf(filter));
    }

    private static BloomFilter initBloomFilter() throws Exception {
        FileReader reader = new FileReader("objs");
        BufferedReader br = new BufferedReader(reader);
        BloomFilter filter = new BloomFilter();

        String line;
        int error = 0;
        int success = 0;
        int total = 0;
        long start = System.currentTimeMillis();
        System.out.println("正在读取数据并初始化过滤器......");
        while ((line = br.readLine()) != null) {
            total++;
            try {
                JSONObject jsonObject = JSONObject.parseObject(line);
                String id = jsonObject.get("other_info").toString();
                filter.add(id);
                success++;
            } catch (Exception e) {
                error++;
            }
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms");
        System.out.println("总数：" + total);
        System.out.println("读取成功数：" + success);
        System.out.println("读取错误数：" + error);
        System.out.println("过滤器中的数目：" + filter.getCount());
        if (filter.contains("BUG")) {
            System.out.println("有BUG");
        }
        return filter;
    }

    private static HashSet initHashSet() throws Exception {
        FileReader reader = new FileReader("objs");
        BufferedReader br = new BufferedReader(reader);
        HashSet<String> hashSet = new HashSet<>();

        String line;
        int error = 0;
        int success = 0;
        int total = 0;
        long start = System.currentTimeMillis();
        System.out.println("正在读取数据并初始化HashSet......");
        while ((line = br.readLine()) != null) {
            total++;
            try {
                JSONObject jsonObject = JSONObject.parseObject(line);
                String id = jsonObject.get("other_info").toString();
                hashSet.add(id);
                success++;
            } catch (Exception e) {
                error++;
            }
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms");
        System.out.println("总数：" + total);
        System.out.println("读取成功数：" + success);
        System.out.println("读取错误数：" + error);
        System.out.println("Hash中的数目：" + hashSet.size());
        if (hashSet.contains("BUG")) {
            System.out.println("有BUG");
        }
        return hashSet;
    }
}
