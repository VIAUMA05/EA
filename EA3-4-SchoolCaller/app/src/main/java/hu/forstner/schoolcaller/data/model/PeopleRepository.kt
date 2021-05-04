package hu.forstner.schoolcaller.data.model

import hu.forstner.schoolcaller.R

class PeopleRepository {

        var list : MutableList<People> = mutableListOf()


        init{
            list.add(People("Tóth Ottó", R.drawable.tothotto, "QEXgJ8nWzwg"))
            list.add(People("Szabó Mária", R.drawable.szabomari, "TQUut8S9SRc"))
            list.add(People("Kovács Piroska", R.drawable.kovacspiri, "u13VTCjU2tk"))
            list.add(People("Gézengúz Gusztáv", R.drawable.gezenguzguszti, "u13VTCjU2tk"))
            list.add(People("Laki Sándor", R.drawable.tothotto, "u13VTCjU2tk"))
            list.add(People("Töröcskei Tímea", R.drawable.szabomari, "u13VTCjU2tk"))
            list.add(People("Vág Hajnalka", R.drawable.kovacspiri, "u13VTCjU2tk"))
            list.add(People("Perec András", R.drawable.gezenguzguszti, "u13VTCjU2tk"))
        }

}