import React, { useState, useEffect } from 'react';
import { View, Text, FlatList, StyleSheet, ActivityIndicator, RefreshControl, TouchableOpacity, TextInput } from 'react-native';
import { useMobileListCustomers } from '../api/hooks/useMobileListCustomers';
import { useNavigation } from '@react-navigation/native';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { CustomersStackParamList } from '../navigation/RootNavigator';
import { useTheme } from '../theme/ThemeProvider';
import { CustomerResponse } from '../api/generated/models';

type NavigationProp = NativeStackNavigationProp<CustomersStackParamList, 'CustomersList'>;

export function CustomersScreen() {
  const theme = useTheme();
  const navigation = useNavigation<NavigationProp>();
  const [page, setPage] = useState(0);
  const [searchInput, setSearchInput] = useState('');
  const [debouncedSearch, setDebouncedSearch] = useState('');

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedSearch(searchInput);
      setPage(0); // Reset page on new search
    }, 500);
    return () => clearTimeout(handler);
  }, [searchInput]);

  const { data, isLoading, isError, error, refetch, isRefetching } = useMobileListCustomers({
    page,
    size: 20,
    sort: ['createdAt,desc'],
    name: debouncedSearch || undefined,
  });

  const isForbidden = (error as any)?.status === 403;

  const renderItem = ({ item }: { item: CustomerResponse }) => (
    <TouchableOpacity 
      style={[styles.card, { backgroundColor: theme.colors.background, borderColor: theme.colors.border }]} 
      onPress={() => navigation.navigate('CustomerDetails', { customerId: item.id! })}
      accessibilityLabel={`View details for customer ${item.name}`}
      accessibilityRole="button"
    >
      <View style={styles.cardHeader}>
        <Text style={[theme.typography.h2, { color: theme.colors.text }]}>{item.name}</Text>
        <View style={[styles.badge, { backgroundColor: theme.colors.surface }]}>
          <Text style={[theme.typography.caption, { color: theme.colors.text }]}>{item.customerType}</Text>
        </View>
      </View>
      <View style={styles.cardBody}>
        <Text style={[theme.typography.body, { color: theme.colors.textSecondary }]}>Status: {item.status}</Text>
        {!!item.contactInfo && <Text style={[theme.typography.body, { color: theme.colors.textSecondary }]}>{item.contactInfo}</Text>}
      </View>
    </TouchableOpacity>
  );

  if (isForbidden) {
    return (
      <View style={[styles.centered, { backgroundColor: theme.colors.background }]}>
        <Text style={[theme.typography.h2, { color: theme.colors.error, marginBottom: theme.spacing.md }]}>Access Denied</Text>
        <Text style={[theme.typography.body, { color: theme.colors.textSecondary, textAlign: 'center' }]}>
          You do not have permission to view customers.
        </Text>
      </View>
    );
  }

  const policies = data?.data?.content || [];
  const totalPages = data?.data?.totalPages || 0;
  const isFirst = data?.data?.first ?? true;
  const isLast = data?.data?.last ?? true;

  return (
    <View style={[styles.container, { backgroundColor: theme.colors.surface }]}>
      <View style={[styles.searchContainer, { backgroundColor: theme.colors.background, borderBottomColor: theme.colors.border }]}>
        <TextInput
          style={[styles.searchInput, theme.typography.body, { backgroundColor: theme.colors.surface, color: theme.colors.text }]}
          placeholder="Search by name..."
          placeholderTextColor={theme.colors.textSecondary}
          value={searchInput}
          onChangeText={setSearchInput}
          accessibilityLabel="Search customers"
        />
      </View>

      {isLoading && !isRefetching ? (
        <View style={[styles.centered, { backgroundColor: theme.colors.surface }]}>
          <ActivityIndicator size="large" color={theme.colors.primary} accessibilityLabel="Loading customers" />
        </View>
      ) : isError ? (
        <View style={[styles.centered, { backgroundColor: theme.colors.surface }]}>
          <Text style={[theme.typography.body, { color: theme.colors.error, marginBottom: theme.spacing.md }]}>Failed to load customers. Please try again.</Text>
          <TouchableOpacity 
            style={[styles.button, { backgroundColor: theme.colors.primary }]} 
            onPress={() => refetch()}
            accessibilityRole="button"
          >
            <Text style={[theme.typography.body, { color: theme.colors.background }]}>Retry</Text>
          </TouchableOpacity>
        </View>
      ) : (
        <FlatList
          data={policies}
          keyExtractor={(item) => item.id || Math.random().toString()}
          renderItem={renderItem}
          contentContainerStyle={{ padding: theme.spacing.md }}
          refreshControl={
            <RefreshControl refreshing={isRefetching} onRefresh={refetch} tintColor={theme.colors.primary} />
          }
          ListEmptyComponent={
            <View style={[styles.centered, { padding: theme.spacing.xl }]}>
              <Text style={[theme.typography.body, { color: theme.colors.textSecondary }]}>No customers found.</Text>
            </View>
          }
          ListFooterComponent={
            policies.length > 0 ? (
              <View style={[styles.pagination, { marginTop: theme.spacing.md }]}>
                <TouchableOpacity
                  style={[styles.button, styles.pageButton, { backgroundColor: isFirst ? theme.colors.border : theme.colors.primary }]}
                  disabled={isFirst}
                  onPress={() => setPage(p => Math.max(0, p - 1))}
                  accessibilityRole="button"
                  accessibilityLabel="Previous page"
                >
                  <Text style={[theme.typography.body, { color: isFirst ? theme.colors.textSecondary : theme.colors.background }]}>Prev</Text>
                </TouchableOpacity>
                <Text style={[theme.typography.body, { color: theme.colors.text }]}>Page {page + 1} of {Math.max(1, totalPages)}</Text>
                <TouchableOpacity
                  style={[styles.button, styles.pageButton, { backgroundColor: isLast ? theme.colors.border : theme.colors.primary }]}
                  disabled={isLast}
                  onPress={() => setPage(p => p + 1)}
                  accessibilityRole="button"
                  accessibilityLabel="Next page"
                >
                  <Text style={[theme.typography.body, { color: isLast ? theme.colors.textSecondary : theme.colors.background }]}>Next</Text>
                </TouchableOpacity>
              </View>
            ) : null
          }
        />
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  searchContainer: { padding: 16, borderBottomWidth: 1 },
  searchInput: { padding: 12, borderRadius: 8 },
  centered: { flex: 1, justifyContent: 'center', alignItems: 'center', padding: 24 },
  card: {
    padding: 16,
    borderRadius: 8,
    marginBottom: 12,
    borderWidth: 1,
    elevation: 1,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.05,
    shadowRadius: 2,
  },
  cardHeader: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 },
  badge: { paddingHorizontal: 8, paddingVertical: 4, borderRadius: 4 },
  cardBody: { gap: 4 },
  button: { paddingVertical: 10, paddingHorizontal: 24, borderRadius: 6, alignItems: 'center' },
  pagination: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  pageButton: { minWidth: 80 },
});
