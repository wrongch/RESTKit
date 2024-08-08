package io.github.wrongch.restkit.config.certificate;

import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.Nullable;

/**
 * CertificateTableModel
 *
 * @author huzunrong
 * @since 2.1.2
 */
public class CertificateTableModel extends ListTableModel<Certificate> {

    public CertificateTableModel() {
        super(
                new CertificateColumnInfo<Boolean>("Enable"),
                new CertificateColumnInfo<String>("Host"),
                new CertificateColumnInfo<String>("PFX file"),
                new CertificateColumnInfo<String>("Passphrase")
        );
    }

    @Override
    public void addRow() {
        addRow(new Certificate());
    }

    public static class CertificateColumnInfo<Aspect> extends ColumnInfo<Certificate, Aspect> {
        public CertificateColumnInfo(String name) {
            super(name);
        }

        @Nullable
        @Override
        @SuppressWarnings("unchecked")
        public Aspect valueOf(Certificate certificate) {
            return switch (getName()) {
                case "Enable" -> (Aspect) certificate.getEnable();
                case "Host" -> (Aspect) certificate.getHost();
                case "PFX file" -> (Aspect) certificate.getPfxFile();
                case "Passphrase" -> (Aspect) certificate.getPassphrase();
                default -> null;
            };
        }

        @Override
        public void setValue(Certificate certificate, Aspect value) {
            switch (getName()) {
                case "Enable":
                    certificate.setEnable((Boolean) value);
                    break;
                case "Host":
                    certificate.setHost((String) value);
                    break;
                case "PFX file":
                    certificate.setPfxFile((String) value);
                    break;
                case "Passphrase":
                    certificate.setPassphrase((String) value);
                    break;
                default:
            }
        }

        @Override
        public Class<?> getColumnClass() {
            if ("Enable".equals(getName())) {
                return Boolean.class;
            }
            return String.class;
        }

        @Override
        public boolean isCellEditable(Certificate certificate) {
            return true;
        }
    }
}