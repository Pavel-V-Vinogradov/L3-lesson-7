@Test
public class TestClass {

    @BeforeSuite
    public void beforeTest(String s) {
        System.out.printf("run @BeforeSuite methode task1(%s)\n", s);
    }

    @Test(priority = 1)
    public void task1(int i) {
        System.out.printf("run @Test methode task1(%s)\n", i);
    }

    @Test(priority = 2)
    public void task2(int i) {
        System.out.printf("run @Test methode task2(%s)\n", i);
    }

    public void task3(int i) {
    }

    @AfterSuite
    public void afterTest(int i) {
        System.out.printf("run @Test methode afterTest(%s)\n", i);
    }

}
