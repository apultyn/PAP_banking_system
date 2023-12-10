package banking_app.classes;

public record Transaction(int transactionId, String title, float amount,
                          int day, int month, int year, char type,
                          int sourceId, int targetId) {
}
