package pl.exsio.querydsl.entityql.entity.scanner;

import java.util.Map;

public class JPAQEntityScannerFactory implements QEntityScannerFactory {
    @Override
    public QEntityScanner createScanner(Map<String, String> params) {
        return new JpaQEntityScanner();
    }
}
