import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AiCommit {
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final String MODEL = "qwen2.5-coder";

    public static void main(String[] args) throws Exception {
        String diff = getStagedDiff();
        if (diff.trim().isEmpty()) {
            System.out.println("Нет проиндексированных файлов для коммита. Сделайте 'git add <files>'");
            System.exit(0);
        }

        System.out.println("Анализ изменений...");
        String commitMsg = generateCommitMessage(diff);

        System.out.println("\nПредлагаемый коммит:\n\033[92m" + commitMsg + "\033[0m\n");

        System.out.print("Принять этот коммит? [Y/n/edit]: ");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.isEmpty() || choice.equals("y") || choice.equals("yes")) {
            executeCommand("git", "commit", "-m", commitMsg);
            System.out.println("Успешно закоммичено!");
        } else if (choice.equals("edit")) {
            System.out.print("Введите новый текст: ");
            String editedMsg = scanner.nextLine().trim();
            executeCommand("git", "commit", "-m", editedMsg);
        } else {
            System.out.println("Отменено.");
        }
    }

    private static String getStagedDiff() throws IOException, InterruptedException {
        Process process = new ProcessBuilder("git", "diff", "--cached").start();
        return new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    private static void executeCommand(String... command) throws IOException, InterruptedException {
        new ProcessBuilder(command).inheritIO().start().waitFor();
    }

    private static String generateCommitMessage(String diff) throws Exception {
        String prompt = "You are an expert developer. Read the following git diff and generate a concise, clear commit message using Conventional Commits format (feat, fix, refactor, docs, etc.). Respond ONLY with the commit message. No explanations, no quotes, no markdown.\n\nDiff:\n" + diff;

        String jsonPayload = String.format("{\"model\":\"%s\",\"prompt\":\"%s\",\"stream\":false}", MODEL, escapeJson(prompt));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Matcher matcher = Pattern.compile("\"response\"\\s*:\\s*\"(.*?)\"(?:,|\\})").matcher(response.body());
        if (matcher.find()) {
            String result = matcher.group(1);
            return result.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\").trim();
        }
        return "Не удалось сгенерировать коммит. Ответ: " + response.body();
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}