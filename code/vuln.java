import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.net.*;
import java.util.zip.*;
import javax.xml.parsers.*;
import org.w3c.dom.Document;

public class VulnerableApp extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        // 1. SQL Injection
        String userId = request.getParameter("userId");
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db", "user", "pass");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE id = '" + userId + "'"); // Sink
        } catch (SQLException e) {}

        // 2. Reflected XSS (Cross-Site Scripting)
        String name = request.getParameter("name");
        response.getWriter().println("<h1>Welcome, " + name + "</h1>"); // Sink

        // 3. Path Traversal (Local File Inclusion)
        String fileName = request.getParameter("file");
        File file = new File("/app/data/" + fileName); // Sink: "../etc/passwd" 입력 시 위험
        FileInputStream fis = new FileInputStream(file);

        // 4. Command Injection
        String bkpCommand = request.getParameter("cmd");
        Runtime.getRuntime().exec("sh backup.sh " + bkpCommand); // Sink

        // 5. Unsafe Deserialization
        try {
            ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
            Object obj = ois.readObject(); // Sink: 임의 객체 실행 위험
        } catch (Exception e) {}

        // 6. Open Redirect
        String url = request.getParameter("targetUrl");
        response.sendRedirect(url); // Sink: 악성 사이트로 리다이렉트 가능

        // 7. XML External Entity (XXE) Injection
        try {
            String xmlData = request.getParameter("xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // 취약한 설정: External Entities 허용
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(xmlData))); // Sink
        } catch (Exception e) {}

        // 8. Log Injection (Log Forging)
        String userLevel = request.getParameter("level");
        System.out.println("User Level set to: " + userLevel); // Sink: 로그 조작 및 가짜 엔트리 삽입

        // 9. Hardcoded Credentials
        String dbPassword = "SecretPassword123!"; // Critical: 하드코딩된 비밀번호
        
        // 10. Zip Slip (Arbitrary File Write via Archive)
        try {
            ZipInputStream zis = new ZipInputStream(request.getInputStream());
            ZipEntry entry = zis.getNextEntry();
            // entry.getName() 검증 없이 파일 경로 생성 시 상위 디렉토리 침투 가능
            File targetFile = new File("/tmp/uploads", entry.getName()); 
            FileOutputStream fos = new FileOutputStream(targetFile); // Sink
        } catch (Exception e) {}
    }
}
