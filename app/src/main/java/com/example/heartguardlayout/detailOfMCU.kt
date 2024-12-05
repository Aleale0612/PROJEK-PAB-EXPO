import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class detailOfMCU : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Data Dummy
        val data = listOf(
            RowData("30 September 2024", "Jakarta", "File.pdf"),
            RowData("29 September 2024", "Bandung", "Document.docx"),
            RowData("28 September 2024", "Surabaya", "Report.pdf")
        )

        // Adapter
        val adapter = RowAdapter(data)
        recyclerView.adapter = adapter
    }
}
