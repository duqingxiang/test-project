import com.test.groovy.SettleTest;

Object hello(from, to, amount) {
    def s1 = new SettleTest(from, 1, amount);
    def s2 = new SettleTest(to, 2, amount);
    List list = new ArrayList();
    list.add(s1); list.add(s2);
    return list;
}