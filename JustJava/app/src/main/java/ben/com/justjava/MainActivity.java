package ben.com.justjava;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;


/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void setLanguage(View view) {
        String languageToLoad = "us";
        if ( ((RadioButton)findViewById(R.id.English)).isChecked()) {
            languageToLoad = "us";
        } else if ( ((RadioButton)findViewById(R.id.Korean)).isChecked()) {
            languageToLoad = "ko";
        }

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        restartActivity();
    }

    public void increment(View view) {
        if (quantity >= 100) {
            Toast.makeText(
                    this, getString(R.string.error_too_much), Toast.LENGTH_LONG
            ).show();
            return;
        }

        quantity++;
        display();
    }

    public void decrement(View view) {
        if (quantity <= 1) {
            Toast.makeText(
                    this, getString(R.string.error_too_little), Toast.LENGTH_LONG
            ).show();
            return;
        }

        quantity--;
        display();
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display() {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        String orderSummary;
        try {
            orderSummary = createOrderSummary();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        displayMessage(orderSummary);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        intent.putExtra(Intent.EXTRA_TEXT, orderSummary);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int basePrice = 5;
        if (addWhippedCream)
            basePrice += 1;
        if (addChocolate)
            basePrice += 2;
        return basePrice * quantity;
    }

    private String createOrderSummary() throws IOException {
        CheckBox whippedCream = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        CheckBox chocolate = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean addWhippedCream = whippedCream.isChecked();
        boolean addChocolate = chocolate.isChecked();
        String customerName = ((EditText) findViewById(R.id.name)).toString();
        if (customerName.equals(R.string.your_name)) {
            // User did not change default input hint
            throw new IOException(getString(R.string.error_name));
        }
        String message = getString(R.string.email_customer) + "\n" +
                getString(R.string.email_add_whipped_cream) + addWhippedCream + "\n" +
                getString(R.string.email_add_chocolate) + addChocolate + "\n" +
                getString(R.string.email_quantity) + quantity + "\n" +
                getString(R.string.email_total) + calculatePrice(addWhippedCream, addChocolate) + "\n" +
                getString(R.string.thank_you);
        return message;
    }

    /**
     * This method displays the given order summary on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

}