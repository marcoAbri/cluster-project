package elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ElasticClient {

    private static ElasticClient istanza;
    private static TransportClient client;

    private ElasticClient() throws UnknownHostException {
        //Settings settings = ImmutableSettings.settingsBuilder().put("client.transport.sniff", true).put("client.transport.ignore_cluster_name", true).build();
        Settings settings = Settings.settingsBuilder().put("client.transport.sniff", true).put("client.transport.ignore_cluster_name", true).build();
       // this.client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
    //this.client= TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        this.client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }

    public static ElasticClient getInstance() throws UnknownHostException {
        if (istanza == null) {
            istanza = new ElasticClient();
        }
        return istanza;
    }

    public TransportClient getClient() {
        return client;
    }

}