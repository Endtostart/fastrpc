public class SampleImpl implements Sample{
    @Override
    public void doSomthing() {

    }

    @Override
    public String backHello(String str) {
        return null;
    }

    @Override
    public Person getRequest() {
        Person person = new Person();
        person.setAge(20);
        person.setName("trojan");
        return person;
    }
}
