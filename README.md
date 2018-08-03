**KarmaKoma**

*https://en.wikipedia.org/wiki/Karmacoma*


**Use**

Annotate your class test with:
`ExperimentTarget`

KarmaKoma will generate class like:


    class Experimented_KotlinTest : KotlinTest() {
       @AnnotationB
       @Test
       fun TEST_testAssert() {
          testAssert()
       }


**Pending**

  Aggregate attributes annotations
  Override annotations

