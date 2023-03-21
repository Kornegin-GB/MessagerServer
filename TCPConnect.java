import java.io.*;
import java.net.Socket;

/**
 * Подключение клиента к серверу посредством TCP подключения
 */
public class TCPConnect implements Runnable {
    private Server server;
    private Socket client;
    private BufferedWriter outMessage;
    private BufferedReader inMessage;

    /**
     * Интерфейс для работы с потоками
     */
    public TCPConnect(Socket client, Server server) {
        try {
            this.server = server;
            this.client = client;
            this.outMessage = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            this.inMessage = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Переопределение метода run() вызываемого при запуске потока
     */
    @Override
    public void run() {
        try {
            server.sendMessageToAllClient("Подключение " + client.getInetAddress() + ":" + client.getPort());
            while (true) {
                String mess = inMessage.readLine();
                if (mess.equalsIgnoreCase("exit")) {
                    break;
                }
                System.out.println(mess);
                server.sendMessageToAllClient(mess);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            this.closeClient();
        }
    }

    /**
     * Метод для отправки сообщения текущему пользователю
     */
    public void sendMessage(String message) {
        try {
            outMessage.write(message);
            outMessage.newLine();
            outMessage.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод закрытия потока при отключении пользователя
     */
    public void closeClient() {
        server.removeClient(this);
        server.sendMessageToAllClient("Отключение " + client.getInetAddress() + ":" + client.getPort());
    }
}
