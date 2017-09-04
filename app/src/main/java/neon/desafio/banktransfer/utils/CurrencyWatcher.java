package neon.desafio.banktransfer.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.concretesolutions.canarinho.formatador.Formatador;

public class CurrencyWatcher implements TextWatcher {
    private final boolean mantemZerosAoLimpar;
    private final Formatador formatador;
    private boolean mudancaInterna;
    private EditText editText;

    public CurrencyWatcher(EditText editText, boolean comSimboloReal, boolean mantemZerosAoLimpar) {
        this.formatador = comSimboloReal
                ? Formatador.VALOR_COM_SIMBOLO
                : Formatador.VALOR;
        this.mantemZerosAoLimpar = mantemZerosAoLimpar;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {

        if (mudancaInterna) {
            return;
        }

        final String somenteNumeros = Formatador.Padroes.PADRAO_SOMENTE_NUMEROS
                .matcher(s.toString())
                .replaceAll("");

        // afterTextChanged é chamado ao rotacionar o dispositivo,
        // essa condição evita que ao rotacionar a tela com o campo vazio ocorra NumberFormatException
        if (somenteNumeros.length() == 0) {
            if (mantemZerosAoLimpar) {
                atualizaTexto(s, formatador.formata("000"));
            }
            return;
        }

        final BigDecimal resultado = new BigDecimal(somenteNumeros)
                .divide(new BigDecimal(100))
                .setScale(2, RoundingMode.HALF_DOWN);

        atualizaTexto(s, formatador.formata(resultado.toPlainString()));
    }

    private void atualizaTexto(Editable s, String valor) {
        mudancaInterna = true;

        editText.setText(valor);
        editText.setSelection(valor.length());

        mudancaInterna = false;
    }
}
