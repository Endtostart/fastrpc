package serialize.support;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.base.Strings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoUtils {
    private static Kryo kryo = new Kryo();
    private KryoUtils() {}

    public static <T> byte[] serialize(T orign) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);
        kryo.writeObject(output, orign);
        output.flush();
        bytes = bos.toByteArray();
        return bytes;
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Input input = new Input(bis);
        T t = kryo.readObject(input, clazz);
        return t;
    }

    public static void main(String[] args) {
        Temp<Dog> temp = new Temp<>();
        temp.setAge(18);
        temp.setName("trojan");
        Dog dog = new Dog();
        dog.setName("旺财");
        temp.setPet(dog);

        byte[] ss = serialize(temp);

        System.out.println("serialize result:" + ss.toString());

        Temp t = deserialize(ss, Temp.class);
        System.out.println("deserialize result: name->" + t.getName() + ",age->" + t.getAge());
        Dog d = (Dog) t.getPet();
        System.out.println("dog's name is :" + d.getName());
    }
}

class Temp<T> {
    private String name;
    private Integer age;
    private T pet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public T getPet() {
        return pet;
    }

    public void setPet(T pet) {
        this.pet = pet;
    }
}

class Dog {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
