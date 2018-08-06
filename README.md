**Kakatua**
*Repeat your tests*

![Car Image](art/kakatua.png)


**Use**

Annotate your class test with:
`ExperimentTarget`

Kakatua will generate class like:


    class Experimented_KotlinTest : KotlinTest() {
       @AnnotationB
       @Test
       fun TEST_testAssert() {
          testAssert()
       }



