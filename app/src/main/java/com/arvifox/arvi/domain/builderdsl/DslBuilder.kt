package com.arvifox.arvi.domain.builderdsl

import com.arvifox.arvi.domain.builderdsl.sdf.Student.Companion.student

object sdf {

    class Student(
        val name: String?,
        val standard: Int,
        val rollNumber: Int
    ) {

        private constructor(builder: Builder) : this(
            builder.name,
            builder.standard,
            builder.rollNumber
        )

        private constructor(builder: Builder2) : this(
            builder.name,
            builder.standard,
            builder.rollNumber
        )

        class Builder {
            var name: String? = null
            var standard: Int = 0
            var rollNumber: Int = 0

            fun name(name: String) = apply { this.name = name }

            fun standard(standard: Int) = apply { this.standard = standard }

            fun rollNumber(rollno: Int) = apply { this.rollNumber = rollno }

            fun build() = Student(this)
        }

        companion object {
            inline fun student(block: Builder2.() -> Unit) =
                Builder2().apply(block).build()
        }

        class Builder2 {
            var name: String? = null
            var standard: Int = 0
            var rollNumber: Int = 0
            fun build() = Student(this)
        }
    }

    fun dsf() = Student.Builder().name("Iasd").standard(23).rollNumber(4545).build()

    fun sdf() = student {
            name = "sdf"
            standard = 34
            rollNumber = 2323
        }
}