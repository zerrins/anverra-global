import React from 'react';
import { View, Text, StyleSheet, ActivityIndicator, ScrollView, RefreshControl } from 'react-native';
import { useRoute, RouteProp } from '@react-navigation/native';
import { useGetCustomer } from '../api/generated/endpoints';
import { CustomersStackParamList } from '../navigation/RootNavigator';
import { useTheme } from '../theme/ThemeProvider';

type CustomerDetailsRouteProp = RouteProp<CustomersStackParamList, 'CustomerDetails'>;

export function CustomerDetailsScreen() {
  const theme = useTheme();
  const route = useRoute<CustomerDetailsRouteProp>();
  const { customerId } = route.params;

  const { data, isLoading, isError, error, refetch, isRefetching } = useGetCustomer(customerId);
  const isForbidden = (error as any)?.status === 403;
  const isNotFound = (error as any)?.status === 404;

  if (isForbidden) {
    return (
      <View style={[styles.centered, { backgroundColor: theme.colors.background }]}>
        <Text style={[theme.typography.h2, { color: theme.colors.error, marginBottom: theme.spacing.md }]}>Access Denied</Text>
        <Text style={[theme.typography.body, { color: theme.colors.textSecondary, textAlign: 'center' }]}>
          You do not have permission to view this customer.
        </Text>
      </View>
    );
  }

  if (isNotFound) {
    return (
      <View style={[styles.centered, { backgroundColor: theme.colors.background }]}>
        <Text style={[theme.typography.h2, { color: theme.colors.error, marginBottom: theme.spacing.md }]}>Customer Not Found</Text>
        <Text style={[theme.typography.body, { color: theme.colors.textSecondary, textAlign: 'center' }]}>
          The customer you are looking for does not exist.
        </Text>
      </View>
    );
  }

  if (isLoading && !isRefetching) {
    return (
      <View style={[styles.centered, { backgroundColor: theme.colors.surface }]}>
        <ActivityIndicator size="large" color={theme.colors.primary} accessibilityLabel="Loading customer details" />
      </View>
    );
  }

  if (isError) {
    return (
      <View style={[styles.centered, { backgroundColor: theme.colors.surface }]}>
        <Text style={[theme.typography.body, { color: theme.colors.error, marginBottom: theme.spacing.md }]}>Failed to load customer details.</Text>
      </View>
    );
  }

  const customer = data?.data;
  if (!customer) return null;

  return (
    <ScrollView 
      style={[styles.container, { backgroundColor: theme.colors.surface }]}
      refreshControl={<RefreshControl refreshing={isRefetching} onRefresh={refetch} tintColor={theme.colors.primary} />}
    >
      <View style={[styles.section, { backgroundColor: theme.colors.background, borderBottomColor: theme.colors.border }]}>
        <Text style={[styles.label, theme.typography.caption, { color: theme.colors.textSecondary }]}>Customer Name</Text>
        <Text style={[theme.typography.h2, { color: theme.colors.text }]}>{customer.name}</Text>
      </View>

      <View style={[styles.section, { backgroundColor: theme.colors.background, borderBottomColor: theme.colors.border }]}>
        <Text style={[styles.label, theme.typography.caption, { color: theme.colors.textSecondary }]}>Status</Text>
        <View style={[styles.statusBadge, { backgroundColor: theme.colors.surface }]}>
          <Text style={[theme.typography.body, { color: theme.colors.text }]}>{customer.status}</Text>
        </View>
      </View>

      <View style={[styles.section, { backgroundColor: theme.colors.background, borderBottomColor: theme.colors.border }]}>
        <Text style={[styles.label, theme.typography.caption, { color: theme.colors.textSecondary }]}>Customer Type</Text>
        <Text style={[theme.typography.body, { color: theme.colors.text }]}>{customer.customerType}</Text>
      </View>

      <View style={[styles.section, { backgroundColor: theme.colors.background, borderBottomColor: theme.colors.border }]}>
        <Text style={[theme.typography.h2, { color: theme.colors.text, marginBottom: theme.spacing.md }]}>Contact Information</Text>
        
        <Text style={[theme.typography.body, { color: theme.colors.text, marginBottom: theme.spacing.sm }]}>{customer.contactInfo || 'N/A'}</Text>
        <Text style={[theme.typography.body, { color: theme.colors.text }]}>{customer.addressInfo || 'N/A'}</Text>
      </View>

      {(customer.individualInfo || customer.businessInfo) && (
        <View style={[styles.section, { backgroundColor: theme.colors.background, borderBottomColor: theme.colors.border }]}>
          <Text style={[theme.typography.h2, { color: theme.colors.text, marginBottom: theme.spacing.md }]}>Additional Information</Text>
          
          {customer.individualInfo && (
             <Text style={[theme.typography.body, { color: theme.colors.text, marginBottom: theme.spacing.sm }]}>{customer.individualInfo}</Text>
          )}
          {customer.businessInfo && (
             <Text style={[theme.typography.body, { color: theme.colors.text }]}>{customer.businessInfo}</Text>
          )}
        </View>
      )}

      <View style={[styles.section, { backgroundColor: theme.colors.background, borderBottomColor: theme.colors.border }]}>
        <Text style={[styles.label, theme.typography.caption, { color: theme.colors.textSecondary }]}>Created At</Text>
        <Text style={[theme.typography.body, { color: theme.colors.text, marginBottom: theme.spacing.sm }]}>{customer.createdAt}</Text>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  centered: { flex: 1, justifyContent: 'center', alignItems: 'center', padding: 24 },
  section: { padding: 16, borderBottomWidth: 1 },
  label: { textTransform: 'uppercase', marginBottom: 4 },
  statusBadge: { alignSelf: 'flex-start', paddingHorizontal: 8, paddingVertical: 4, borderRadius: 4 },
});
