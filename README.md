**Kakatua**
*Repeat your tests*

![Car Image](art/kakatua.png)


**Use**

Annotate your class test with:
`ExperimentTarget`

Kakatua will generate class like:
Given Test Class:

    @ExperimentTarget([Experiments.EXPERIMENT_2, Experiments.EXPERIMENT_4, Experiments.EXPERIMENT_5])
    open class KotlinTest {

       @Test
       @ReplaceableLabel("ssksk")
       @BExperiments([Experiments.EXPERIMENT_1, Experiments.EXPERIMENT_2])
       fun testAssert() {
           assert(true)
       }

The output will be:

    class Experimented_KotlinTest : KotlinTest() {
        @BExperiments([
                com.agoda.generator.annotations.Experiments.EXPERIMENT_2,
                com.agoda.generator.annotations.Experiments.EXPERIMENT_4,
                com.agoda.generator.annotations.Experiments.EXPERIMENT_5,
                com.agoda.generator.annotations.Experiments.EXPERIMENT_1
        ])
        @ReplaceableLabel("GENERATED_ssksk")
        @Test
        fun TEST_testAssert() {
            testAssert()
        }



**Pending**

  Override annotations

