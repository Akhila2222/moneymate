package uk.ac.tees.mad.moneymate

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.moneymate.database.AppDatabase
import uk.ac.tees.mad.moneymate.database.ExpenseDao
import uk.ac.tees.mad.moneymate.firestore.FirestoreDataSource
import uk.ac.tees.mad.moneymate.firestore.StorageDataSource
import uk.ac.tees.mad.moneymate.repo.ExpenseRepository
import uk.ac.tees.mad.moneymate.repo.UserRepository
import uk.ac.tees.mad.moneymate.utils.BiometricAuthHelper
import uk.ac.tees.mad.moneymate.utils.PreferencesManager
import uk.ac.tees.mad.moneymate.utils.dataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context) = Room
        .databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "moneymate_database"
        )
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun providesDao(db: AppDatabase) = db.categoryDao()


    @Provides
    fun provideExpenseDao(db: AppDatabase): ExpenseDao {
        return db.expenseDao()
    }

    @Provides
    @Singleton
    fun provideFirestoreDataSource(): FirestoreDataSource {
        return FirestoreDataSource()
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(
        expenseDao: ExpenseDao,
        firestoreDataSource: FirestoreDataSource,
        storageDataSource: StorageDataSource
    ): ExpenseRepository {
        return ExpenseRepository(expenseDao, firestoreDataSource, storageDataSource)
    }

    @Provides
    @Singleton
    fun provideStorageDataSource(): StorageDataSource {
        return StorageDataSource()
    }

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context.dataStore)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        storageDataSource: StorageDataSource,
        firestore: FirebaseFirestore
    ): UserRepository {
        return UserRepository(firestore, storageDataSource)
    }

    @Provides
    @Singleton
    fun provideBiometricAuthHelper(@ApplicationContext context: Context): BiometricAuthHelper {
        return BiometricAuthHelper(context)
    }
}
