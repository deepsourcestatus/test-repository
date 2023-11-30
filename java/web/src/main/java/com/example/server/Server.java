package com.example.server;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;
import java.sql.*;



public class Server extends HttpServlet {

    static final String DB_URL = "jdbc:mysql://localhost/users";
    public Connection conn;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie c = new Cookie("uid", req.getSession().getId());
        // For older browsers?
        c.setSecure(false);
        resp.addCookie(c);
        resp.setHeader("Access-Control-Allow-Origin", "*");

        Boolean b = Boolean.parseBoolean(req.getParameter("winCondition"));

        try {
            conn =
                    conn = DriverManager.getConnection(DB_URL, "user", "");
            Statement s = conn.createStatement();
            s.execute("SELECT userName, isWin FROM users WHERE uid = " + req.getParameter("ticket") + ";");
            ResultSet r = s.getResultSet();

            URI offerAPI = new URI(req.getParameter("offerAPI"));

            String id = r.getString(0);

            if (r.getBoolean("isWin") && b) {
                resp.getWriter().print("You win, " + r.getString("userName") + "!<br>You can fill your details in with this link: " + req.getParameter(id));
                StringBuffer sb = new StringBuffer();
                HttpClient hc = HttpClient.newBuilder().build();
                HttpRequest offerReq = HttpRequest.newBuilder(offerAPI).GET().build();
                Cipher ci = Cipher.getInstance("DES/ECB/PKCS5Padding");

                HttpResponse<String> offerResp = hc.send(offerReq, HttpResponse.BodyHandlers.ofString());

                if (offerResp.body() != null) {
                    resp.getWriter().print("An additional offer is available only for you!" + req.getParameter("offerId"));
                }

                resp.getWriter().write("You win, " + r.getString("userName") + "!<br>You can fill your details in with this link: " + req.getParameter(id));
            } else {
                resp.getWriter().print("You lose, " + r.getString("userName"));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } catch (URISyntaxException e) {} catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }  catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        System.gc();
        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    public void destroy() {
        super.destroy();
    }


    @Override
    public void init() throws ServletException {
        super.init();

        try {
            conn = DriverManager.getConnection(DB_URL, "user", "");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            return;
        }

    }
}

class BubbleSort implements SortAlgorithm {
    @Override
    public <T extends Comparable<T>> T[] sort(T[] array) {
        for (int i = 1, size = array.length; i < size; ++i) {
            boolean swapped = false;
            for (int j = 0; j < size - i; ++j) {
                if (greater(array[j], array[j + 1])) {
                    swap(array, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
        return array;
    }

    public static void main(String[] args) {

        Integer[] integers = {4, 23, 6, 78, 1, 54, 231, 9, 12};
        BubbleSort bubbleSort = new BubbleSort();
        bubbleSort.sort(integers);

        for (int i = 0; i < integers.length - 1; ++i) {
            assert integers[i] <= integers[i + 1];
        }
        print(integers);
        /* output: [1, 4, 6, 9, 12, 23, 54, 78, 231] */

        String[] strings = {"c", "a", "e", "b", "d"};
        bubbleSort.sort(strings);
        for (int i = 0; i < strings.length - 1; i++) {
            assert strings[i].compareTo(strings[i + 1]) <= 0;
        }
        print(bubbleSort.sort(strings));
        /* output: [a, b, c, d, e] */
    }
}