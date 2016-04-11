package com.example.android.primenumbers;


import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import java.util.ArrayList;
import java.util.Arrays;

public class PrimeActivity extends AppCompatActivity {


    public static ArrayList<String> total_primes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    //Дествие при нажатие на кнопку GO
    public void rangeAction(View view) {
        EditText first_number = (EditText) findViewById(R.id.first_number);
        EditText second_number = (EditText) findViewById(R.id.second_number);
        int a = Integer.parseInt(first_number.getText().toString());
        int b = Integer.parseInt(second_number.getText().toString());
        //Нахождение простых чисел в диапазоне от a до b
        calculate(a, b);

        // Предварительная проверка границ [a, b]
        if (a <= 0) {

            //Никогда не будет вызван, ввод разрещен только положительным числам
            Snackbar.make(view, "A > 0", Snackbar.LENGTH_SHORT).show();
        } else if (b <= a) Snackbar.make(view, "A < B", Snackbar.LENGTH_SHORT).show();
        else if (total_primes.size()==0) {
            Snackbar.make(view, "Нет чисел", Snackbar.LENGTH_SHORT).show();
        }
        else {
            Intent resultIntent = new Intent(this, RecyclerViewActivity.class);
                startActivity(resultIntent);

        }
    }


    public void initializeData(String result) {
        total_primes.add(result);
    }

    // Функция нахождения простых чисел, которой передаются два параметра a и b
    public void calculate(int a, int b) {

        total_primes.clear();

        // SieveOfAtkin
        int limit = b;
        boolean[] sieve = new boolean[limit + 1];
        int limitSqrt = (int) Math.sqrt((double) limit);


        Arrays.fill(sieve, false);
        // the sieve works only for integers > 3, so
        // set these trivially to their proper values
        sieve[0] = false;
        sieve[1] = false;
        sieve[2] = true;
        sieve[3] = true;

        for (int x = 1; x <= limitSqrt; x++) {
            for (int y = 1; y <= limitSqrt; y++) {
                // first quadratic using m = 12 and r in R1 = {r : 1, 5}
                int n = (4 * x * x) + (y * y);
                if (n <= limit && (n % 12 == 1 || n % 12 == 5)) {
                    sieve[n] = !sieve[n];
                }
                // second quadratic using m = 12 and r in R2 = {r : 7}
                n = (3 * x * x) + (y * y);
                if (n <= limit && (n % 12 == 7)) {
                    sieve[n] = !sieve[n];
                }
                // third quadratic using m = 12 and r in R3 = {r : 11}
                n = (3 * x * x) - (y * y);
                if (x > y && n <= limit && (n % 12 == 11)) {
                    sieve[n] = !sieve[n];
                } // end if
                // note that R1 union R2 union R3 is the set R
                // R = {r : 1, 5, 7, 11}
                // which is all values 0 < r < 12 where r is
                // a relative prime of 12
                // Thus all primes become candidates
            } // end for
        } // end for
        // remove all perfect squares since the quadratic
        // wheel factorization filter removes only some of them
        for (int n = 5; n <= limitSqrt; n++) {
            if (sieve[n]) {
                int x = n * n;
                for (int i = x; i <= limit; i += x) {
                    sieve[i] = false;
                } // end for
            } // end if
        } // end for

        for (int i = a; i <= b; i++) {
            if (sieve[i]) {
                initializeData(String.valueOf(i));
            } // end if
        } // end for


    }

}


