package su.arq.basicsqlcrud

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_row.*

class RowActivity : AppCompatActivity() {

    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_row)

//
//        intent.putExtra("MainActId", row.id)
//        intent.putExtra("MainActFamily", row.family)
//        intent.putExtra("MainActNumSp", row.number_of_species)
//        intent.putExtra("MainActPerSp", row.percent_of_species)
//        intent.putExtra("MainActNumGen", row.number_of_genus)
//        intent.putExtra("MainActPerGen", row.percent_of_genus)

        try {
            var bundle: Bundle? = intent.extras
            id = bundle!!.getInt("MainActId", 0)
            if (id != 0) {
                edt_family.setText(bundle.getString("MainActFamily"))
                edt_species_num.setText(bundle.getInt("MainActNumSp").toString())
                edt_species_percent.setText(bundle.getDouble("MainActPerSp").toString())
                edt_genus_num.setText(bundle.getInt("MainActNumGen").toString())
                edt_genus_percent.setText(bundle.getDouble("MainActPerGen").toString())
            }
        } catch (ex: Exception) {
        }

        btAdd.setOnClickListener {
            var dbManager = DBManager(this)

            var values = ContentValues()
            values.put("FAMILY", edt_family.text.toString())
            values.put("NUMBER_OF_SPECIES", Integer.parseInt( edt_species_num.text.toString()))
            values.put("PERCENT_OF_SPECIES",  edt_species_percent.text.toString().toDouble())
            values.put("NUMBER_OF_GENUS", Integer.parseInt( edt_genus_num.text.toString()))
            values.put("PERCENT_OF_GENUS",  edt_genus_percent.text.toString().toDouble())


            if (id == 0) {
                val mID = dbManager.insert(values)

                if (mID > 0) {
                    Toast.makeText(this, "Add note successfully! id:$mID", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Fail to add note! id:$mID", Toast.LENGTH_LONG).show()
                }
            } else {
                var selectionArs = arrayOf(id.toString())
                val mID = dbManager.update(values, "ID=?", selectionArs)

                if (mID > 0) {
                    Toast.makeText(this, "Add note successfully! id:$mID", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Fail to add note! id:$mID", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
