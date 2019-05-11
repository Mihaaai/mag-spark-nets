import model.{Author, Fos, FosChildren, MAG, Paper, PaperAuthorAff, PaperFos, PaperReference}
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.functions.collect_list
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}
import utils.SparkUtils



object Main {

  def main(args: Array[String]): Unit = {
    val utils = new SparkUtils
    val spark = utils.spark
    val mag  = "/home/bob/gtd/Iconic/data/samples"

    val paperSchema = ScalaReflection.schemaFor[Paper].dataType.asInstanceOf[StructType]
    lazy val dfPapers = utils.getDataFrame(mag + "/Papers.txt", paperSchema)

    val authorSchema = ScalaReflection.schemaFor[Author].dataType.asInstanceOf[StructType]
    lazy val dfAuthors = utils.getDataFrame(mag + "/Authors.txt", authorSchema)

    val fosSchema = ScalaReflection.schemaFor[Fos].dataType.asInstanceOf[StructType]
    lazy val dfFos = utils.getDataFrame(mag + "/FieldsOfStudy.txt", fosSchema)

    val paperAuthorAffSchema = ScalaReflection.schemaFor[PaperAuthorAff].dataType.asInstanceOf[StructType]
    lazy val dfPaperAuthorAff = utils.getDataFrame(mag + "/PaperAuthorAffiliations.txt", paperAuthorAffSchema)

    val fosChildrenSchema = ScalaReflection.schemaFor[FosChildren].dataType.asInstanceOf[StructType]
    lazy val dfFosChildren = utils.getDataFrame(mag + "/FieldOfStudyChildren.txt", fosChildrenSchema)

    val paperFosSchema = ScalaReflection.schemaFor[PaperFos].dataType.asInstanceOf[StructType]
    lazy val dfPaperFos = utils.getDataFrame(mag + "/PaperFieldsOfStudy.txt", paperFosSchema)

    val paperReferenceSchema = ScalaReflection.schemaFor[PaperReference].dataType.asInstanceOf[StructType]
    lazy val dfPaperReferences = utils.getDataFrame(mag + "/PaperReferences.txt", paperReferenceSchema)

    val x = dfAuthors.select("dname").take(10)
    print(x)

    /*todo : get major field of study for each author
      - map each field of study to its parent
      = PAA join PF on PAA.paper -> {author, paper, fos}
      => map  -> {author, paper, root_fos}
      => group by author -> {author, [root_fos]}
      => aggregate -> {author, [(occurences, root_fos)]}
    */

    //    val logFile = "/home/bob/gtd/Iconic/test.txt" // Should be some file on your system
//    val logData = spark.read.textFile(logFile).cache()
//    val numAs = logData.filter(line => line.contains("a")).count()
//    val numBs = logData.filter(line => line.contains("b")).count()
//    println(s"Lines with a: $numAs, Lines with b: $numBs")
    spark.stop()
  }
}