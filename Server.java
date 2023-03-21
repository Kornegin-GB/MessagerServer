import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс описывает логику работы сервера
 */
public class Server {
    public static void main(String[] args) {
        Server server = new Server();
    }

    private final List<TCPConnect> clientList = new ArrayList<TCPConnect>(); // Список всех соединений

    public Server() {
        Socket clientSocket = null;
        // Запускаем сервер
        try (ServerSocket serverSocket = new ServerSocket(55555)) {
            System.out.println("Сервер запущен");
            while (true) {
                try {
                    //Ждем подключения пользователей
                    clientSocket = serverSocket.accept();
                    TCPConnect connect = new TCPConnect(clientSocket, this); //Создаем поток для каждого соединения
                    clientList.add(connect);
                    new Thread(connect).start();// Запускаем поток
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Метод для отправки сообщения всем подключившемся пользователям
     */
    public void sendMessageToAllClient(String message) {
        for (TCPConnect user : clientList) {
            user.sendMessage(message);
        }
    }

    /**
     * Метод удаления пользователя из списка подключившихся
     */
    public void removeClient(TCPConnect connect) {
        clientList.remove(connect);
    }
}