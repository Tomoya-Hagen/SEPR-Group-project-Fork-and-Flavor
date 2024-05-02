
CREATE TABLE IF NOT EXISTS "User" (
	"id" uuid NOT NULL,
	"username" varchar(100) NOT NULL,
	"email" varchar(100) NOT NULL,
	"password" char(100) NOT NULL,
	"has_profile_picture" boolean,
	PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS "User_Role" (
	"user_id" uuid NOT NULL,
	"role_id" uuid NOT NULL,
	PRIMARY KEY("user_id", "role_id")
);

CREATE TABLE IF NOT EXISTS "Role" (
	"id" uuid NOT NULL,
	"name" varchar(100) NOT NULL,
	PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS "Recipe" (
	"id" uuid NOT NULL,
	"name" varchar(200) NOT NULL,
	"description" text(65535),
	"number_of_servings" smallint NOT NULL,
	"forked_from" uuid,
	"owner_id" uuid NOT NULL,
	"is_draft" boolean NOT NULL,
	PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS "Ingredient" (
	"id" INT NOT NULL,
	"name" varchar(200) NOT NULL,
	PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS "Recipe_Ingredient" (
	"recipe_id" uuid NOT NULL,
	"ingredient_id" INT NOT NULL,
	"amount" NUMERIC(5,2) NOT NULL,
	"unit" varchar(10) NOT NULL,
	PRIMARY KEY("recipe_id", "ingredient_id"),
    CHECK ( "unit" IN ('oz', 'fl oz', 'c', 'pt', 'qt', 'gal', 'tsp', 'tbsp', 'g', 'kg', 'ml', 'L', 'lb') )
);

CREATE TABLE IF NOT EXISTS "Cooked" (
	"user_id" uuid NOT NULL,
	"recipe_id" uuid NOT NULL,
	"date" date NOT NULL,
	PRIMARY KEY("user_id", "recipe_id")
);

CREATE TABLE IF NOT EXISTS "Favorite" (
	"user_id" uuid NOT NULL,
	"recipe_id" uuid NOT NULL,
	PRIMARY KEY("user_id", "recipe_id")
);

CREATE TABLE IF NOT EXISTS "Rating" (
	"user_id" uuid NOT NULL,
	"recipe_id" uuid NOT NULL,
	"cost" NUMERIC(5,2),
	"taste" NUMERIC(1,0) NOT NULL,
	"ease_of_prep" NUMERIC(1,0),
	"review" text(65535),
	PRIMARY KEY("user_id", "recipe_id")
);


CREATE TABLE IF NOT EXISTS "Category" (
	"id" uuid NOT NULL,
	"name" varchar(100) NOT NULL,
	"type" varchar(100),
	PRIMARY KEY("id"),
    CHECK ( "type" IN ('sample') ) /*Add types of categories*/
);

CREATE TABLE IF NOT EXISTS "Recipe_Category" (
	"category_id" uuid NOT NULL,
	"recipe_id" uuid NOT NULL,
	PRIMARY KEY("category_id", "recipe_id")
);

CREATE TABLE  IF NOT EXISTS "Allergen" (
    "id" CHAR(1) NOT NULL,
	"name" varchar(100) NOT NULL,
	"description" text(65535),
	PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS "Ingredient_Allergen" (
	"ingredient_id" INT NOT NULL,
	"allergen_id" CHAR(1) NOT NULL,
	PRIMARY KEY("ingredient_id", "allergen_id")
);

CREATE TABLE IF NOT EXISTS "Nutrition" (
	"id" INT NOT NULL,
	"name" varchar(1000) NOT NULL,
	PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS "Ingredient_Nutrition" (
	"ingredient_id" INT NOT NULL,
	"nutrition_id" INT NOT NULL,
	"unit" VARCHAR(10),
	"value" NUMERIC(5,2) NOT NULL,
	PRIMARY KEY("ingredient_id", "nutrition_id"),
    check ( "unit" IN ('', 'mg', 'g')) /*Add units of nutritions*/
);

CREATE TABLE IF NOT EXISTS "Verified" (
	"id" uuid NOT NULL,
	"name" varchar(100),
	"type" VARCHAR(100),
	PRIMARY KEY("id"),
    CHECK("type" IN ('sample')) /*Add types of verified if needed*/
);

CREATE TABLE IF NOT EXISTS "Recipe_Verified" (
	"id" uuid NOT NULL,
	"recipe_id" uuid,
	"verified_id" uuid,
	"user_id" uuid,
	"is_external" boolean NOT NULL,
	PRIMARY KEY("id")
);


CREATE TABLE IF NOT EXISTS "Weekly_Planner" (
	"id" uuid NOT NULL,
	"user_id" uuid NOT NULL,
	"date" date NOT NULL,
	"daytime" VARCHAR(10) NOT NULL,
	"recipe_id" uuid,
	PRIMARY KEY("id"),
    CHECK ( "daytime" IN ('breakfast', 'brunch', 'lunch', 'dinner', 'supper'))
);

CREATE TABLE IF NOT EXISTS "User_Weekly_Planner" (
	"user_id" uuid NOT NULL,
	"weekly_planner_id" uuid NOT NULL,
	"permission" VARCHAR(10) NOT NULL,
	PRIMARY KEY("user_id", "weekly_planner_id"),
    CHECK ( "permission" IN ('read', 'write', 'delete') )
);

CREATE TABLE IF NOT EXISTS "Recipe_Book" (
	"id" uuid NOT NULL,
	"name" varchar(100) NOT NULL,
	"description" text(65535),
	"owner_id" uuid,
	PRIMARY KEY("id")
);


CREATE TABLE IF NOT EXISTS "User_Recipe_Book" (
	"user_id" uuid NOT NULL,
	"recipe_book_id" uuid NOT NULL,
	"permission" VARCHAR(10) NOT NULL,
	PRIMARY KEY("user_id", "recipe_book_id"),
    CHECK ( "permission" IN ('read', 'write', 'delete') )
);

CREATE TABLE IF NOT EXISTS "Recipe_Recipe_Book" (
	"recipe_book_id" uuid NOT NULL,
	"recipe_id" uuid NOT NULL,
	PRIMARY KEY("recipe_book_id", "recipe_id")
);

CREATE TABLE IF NOT EXISTS "Recipe_Step" (
	"id" uuid NOT NULL,
	"name" varchar(200) NOT NULL,
	"recipe_id" uuid,
	"step_number" int NOT NULL,
	"step_description_id" uuid,
	"step_recipe_id" uuid,
	PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS "Recipe_Description_Step" (
	"id" uuid NOT NULL,
	"description" text(65535) NOT NULL,
	"name" varchar(255) NOT NULL,
	PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS "Recipe_Recipe_Step" (
	"id" uuid NOT NULL,
	"recipe_id" uuid NOT NULL,
	"name" varchar(255),
	PRIMARY KEY("id")
);

ALTER TABLE "User_Role"
ADD FOREIGN KEY("user_id") REFERENCES "User"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "User_Role"
ADD FOREIGN KEY("role_id") REFERENCES "Role"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe"
ADD FOREIGN KEY("id") REFERENCES "User"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe_Ingredient"
ADD FOREIGN KEY("recipe_id") REFERENCES "Recipe"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe_Ingredient"
ADD FOREIGN KEY("ingredient_id") REFERENCES "Ingredient"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Favorite"
ADD FOREIGN KEY("user_id") REFERENCES "User"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Cooked"
ADD FOREIGN KEY("user_id") REFERENCES "User"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Favorite"
ADD FOREIGN KEY("recipe_id") REFERENCES "Recipe"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Cooked"
ADD FOREIGN KEY("recipe_id") REFERENCES "Recipe"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe"
ADD FOREIGN KEY("forked_from") REFERENCES "Recipe"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Rating"
ADD FOREIGN KEY("recipe_id") REFERENCES "Recipe"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Rating"
ADD FOREIGN KEY("user_id") REFERENCES "User"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe_Category"
ADD FOREIGN KEY("category_id") REFERENCES "Category"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe_Category"
ADD FOREIGN KEY("recipe_id") REFERENCES "Recipe"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Ingredient_Allergen"
ADD FOREIGN KEY("ingredient_id") REFERENCES "Ingredient"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Ingredient_Allergen"
ADD FOREIGN KEY("allergen_id") REFERENCES "Allergen"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Ingredient_Nutrition"
ADD FOREIGN KEY("nutrition_id") REFERENCES "Nutrition"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Ingredient_Nutrition"
ADD FOREIGN KEY("ingredient_id") REFERENCES "Ingredient"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe_Verified"
ADD FOREIGN KEY("verified_id") REFERENCES "Verified"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe_Verified"
ADD FOREIGN KEY("recipe_id") REFERENCES "Recipe"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe_Verified"
ADD FOREIGN KEY("user_id") REFERENCES "User"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Weekly_Planner"
ADD FOREIGN KEY("user_id") REFERENCES "User"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Weekly_Planner"
ADD FOREIGN KEY("recipe_id") REFERENCES "Recipe"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "User_Weekly_Planner"
ADD FOREIGN KEY("user_id") REFERENCES "User"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "User_Weekly_Planner"
ADD FOREIGN KEY("weekly_planner_id") REFERENCES "Weekly_Planner"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe_Recipe_Book"
ADD FOREIGN KEY("recipe_book_id") REFERENCES "Recipe_Book"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe_Recipe_Book"
ADD FOREIGN KEY("recipe_id") REFERENCES "Recipe"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "User_Recipe_Book"
ADD FOREIGN KEY("user_id") REFERENCES "User"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "User_Recipe_Book"
ADD FOREIGN KEY("recipe_book_id") REFERENCES "Recipe_Book"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe"
ADD FOREIGN KEY("owner_id") REFERENCES "User"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe_Book"
ADD FOREIGN KEY("owner_id") REFERENCES "User"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe_Step"
ADD FOREIGN KEY("recipe_id") REFERENCES "Recipe"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe_Step"
ADD FOREIGN KEY("step_description_id") REFERENCES "Recipe_Description_Step"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe_Step"
ADD FOREIGN KEY("step_recipe_id") REFERENCES "Recipe_Recipe_Step"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE "Recipe_Recipe_Step"
ADD FOREIGN KEY("recipe_id") REFERENCES "Recipe"("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;