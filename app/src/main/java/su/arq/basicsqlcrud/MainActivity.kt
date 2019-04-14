package su.arq.basicsqlcrud

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {

    var rows = ArrayList<Family>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadQueryAll()


        addBtn.setOnClickListener {
            val dbManager = DBManager(this)
            val cursor =  dbManager.queryAll()
            updateRow(Family(cursor.count,"Family", 0,0.0,0,0.0))
        }

//        rows = createDummy()

//        val adapter = RowsAdapter(rows, this)
//        list_view.adapter = adapter
    }

    fun loadQueryAll() {

        var dbManager = DBManager(this)
        val cursor = dbManager.queryAll()

        rows.clear()
        if (cursor.moveToFirst()) {

            do {
                val id = cursor.getInt(cursor.getColumnIndex("ID"))
                val family = cursor.getString(cursor.getColumnIndex("FAMILY"))
                val num_of_sp = cursor.getInt(cursor.getColumnIndex("NUMBER_OF_SPECIES"))
                val perc_of_sp = cursor.getDouble(cursor.getColumnIndex("PERCENT_OF_SPECIES"))
                val num_of_gen = cursor.getInt(cursor.getColumnIndex("NUMBER_OF_GENUS"))
                val perc_of_gen = cursor.getDouble(cursor.getColumnIndex("PERCENT_OF_GENUS"))

                rows.add(Family(id, family, num_of_sp, perc_of_sp, num_of_gen, perc_of_gen))

            } while (cursor.moveToNext())
        }

//        //TODO("delete dummy")
//        rows = createDummy()

        var notesAdapter = RowsAdapter(rows, this)
        list_view.adapter = notesAdapter
    }

    fun createDummy() : ArrayList<Family>{
        val rows = ArrayList<Family>()
        for(i:Int in 1..100){
            rows.add(Family(i,"Cannabis",69,15.5,14,58.8))
            rows.add(Family(i+1,"Hash",14,14.5,88,5.8))
            rows.add(Family(i+2,"Opium",96,13.5,4,8.8))
            rows.add(Family(i+3,"Coca",71,11.5,14,58.1))
        }
        return rows
    }

    inner class RowsAdapter(var rowsList: ArrayList<Family>, var context: Context) : BaseAdapter(){

//        private var rowsList = ArrayList<Family>()
//        private var context: Context? = null

//        constructor(context: Context, rowsList: ArrayList<Family>): super(){
//            this.rowsList = rowsList
//            this.context = context
//        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val view: View
            val vh: ViewHolder

            if(convertView == null){
                view = layoutInflater.inflate(R.layout.list_item, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
            }
            else{
                view = convertView
                vh = view.tag as ViewHolder
            }

            val row = rowsList[position]
            vh.id.text = row.id.toString()
            vh.family.setText(row.family)
            vh.speciesNum.setText(row.number_of_species.toString())
            vh.percentSpecies.setText(row.percent_of_species.toString())
            vh.genusNum.setText(row.number_of_genus.toString())
            vh.percentGenus.setText(row.percent_of_genus.toString())

            vh.btnDelete.setOnClickListener {
                val dbManager = DBManager(this.context!!)
                val selectionArgs = arrayOf(row.id.toString())
                dbManager.delete("Id=?", selectionArgs)
                loadQueryAll()
            }

            vh.btnEdit.setOnClickListener {
                updateRow(row)
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return rowsList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return rowsList.size
        }
    }

    private fun updateRow(row: Family){
        var intent = Intent(this, RowActivity::class.java)
        intent.putExtra("MainActId", row.id)
        intent.putExtra("MainActFamily", row.family)
        intent.putExtra("MainActNumSp", row.number_of_species)
        intent.putExtra("MainActPerSp", row.percent_of_species)
        intent.putExtra("MainActNumGen", row.number_of_genus)
        intent.putExtra("MainActPerGen", row.percent_of_genus)
        startActivity(intent)
    }

    private class ViewHolder(view: View?) {
        val id: TextView
        val family: TextView
        val speciesNum: TextView
        val percentSpecies: TextView
        val genusNum: TextView
        val percentGenus: TextView
        val btnDelete: Button
        val btnEdit: Button

        init {
            this.id = view?.findViewById(R.id.id_text_view) as TextView
            this.family = view.findViewById(R.id.family_text_view) as TextView
            this.speciesNum = view.findViewById(R.id.species_num_text_view) as TextView
            this.percentSpecies = view.findViewById(R.id.percent_species_text_view) as TextView
            this.genusNum = view.findViewById(R.id.genus_num_text_view) as TextView
            this.percentGenus = view.findViewById(R.id.percent_genus_text_view) as TextView
            this.btnEdit = view.findViewById(R.id.btn_edit) as Button
            this.btnDelete = view.findViewById(R.id.btn_delete) as Button
        }
    }
}



