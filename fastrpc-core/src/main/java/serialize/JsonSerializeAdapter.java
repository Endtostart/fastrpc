package serialize;

public class JsonSerializeAdapter extends DefaultJsonSerialize {
    private JdkSerialize agent;

    public JsonSerializeAdapter(JdkSerialize agent) {
        this.agent = agent;
    }


}
